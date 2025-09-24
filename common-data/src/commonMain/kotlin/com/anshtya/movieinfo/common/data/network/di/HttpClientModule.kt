package com.anshtya.movieinfo.common.data.network.di

import com.anshtya.movieinfo.common.data.BuildKonfig
import com.anshtya.movieinfo.common.data.network.HttpException
import com.anshtya.movieinfo.common.data.network.model.auth.ErrorResponse
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
internal class HttpClientModule {
    @Single
    fun baseHttpClient(): HttpClient {
        return HttpClient { applyDefaultConfiguration() }.config {
            defaultRequest {
                url("https://api.themoviedb.org/3/")
                header(HttpHeaders.Authorization, BuildKonfig.ACCESS_TOKEN)
            }
        }
    }
}

/**
 * Provides a reusable default [HttpClientConfig].
 *
 * This configuration is defined outside of the Koin module so it can be shared
 * across both production and test code.
 */
internal fun HttpClientConfig<*>.applyDefaultConfiguration() {
    expectSuccess = true
    HttpResponseValidator {
        handleResponseException { throwable, _ ->
            throw when (throwable) {
                is ClientRequestException -> {
                    val response = throwable.response
                    if (response.status == HttpStatusCode.Unauthorized) {
                        val errorMessage = response.body<ErrorResponse>().statusMessage
                        Exception(errorMessage)
                    } else {
                        Exception("Can't connect to server", throwable)
                    }
                }

                is ServerResponseException -> Exception("Something went wrong", throwable)
                else -> HttpException("An error occurred", throwable)
            }
        }
    }
    install(ContentNegotiation) {
        json(
            Json {
                isLenient = true
                ignoreUnknownKeys = true
                explicitNulls = false
            }
        )
    }
}