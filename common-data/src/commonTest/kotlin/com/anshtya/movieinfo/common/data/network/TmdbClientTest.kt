package com.anshtya.movieinfo.common.data.network

import com.anshtya.movieinfo.common.data.network.di.applyDefaultConfiguration
import com.anshtya.movieinfo.common.data.network.model.NetworkContent
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TmdbApiTest {
    @Test
    fun testDeserialization() = runTest {
        val mockEngine = MockEngine { request ->
            respond(
                content = ByteReadChannel(testResponse),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
        val tmdbClient = TmdbClient(
            httpClient = HttpClient(mockEngine) { applyDefaultConfiguration() }
        )

        val content = tmdbClient.getMovieLists(category = "", page = 1).getOrNull()!!
        assertEquals(
            NetworkContent(
                id = 640146,
                title = "Ant-Man and the Wasp: Quantumania",
                name = null,
                posterPath = "/ngl2FKBlU4fhbdsrtdom9LVLBXw.jpg",
            ),
            content.results.first()
        )
    }

    @Test
    fun testErrorDeserialization() = runTest {
        val mockEngine = MockEngine { request ->
            respond(
                content = ByteReadChannel("""
                    { "status_message": "Incorrect login" }
                """.trimIndent()),
                status = HttpStatusCode.Unauthorized,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
        val tmdbClient = TmdbClient(
            httpClient = HttpClient(mockEngine) { applyDefaultConfiguration() }
        )

        val result = tmdbClient.getMovieLists(category = "", page = 1)
        assertTrue(result.isFailure)
        assertEquals(
            result.exceptionOrNull()?.message,
            "Incorrect login"
        )
    }
}