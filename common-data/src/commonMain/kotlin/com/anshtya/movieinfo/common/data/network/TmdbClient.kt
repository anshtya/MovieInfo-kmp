package com.anshtya.movieinfo.common.data.network

import com.anshtya.movieinfo.common.data.network.model.FavoriteRequest
import com.anshtya.movieinfo.common.data.network.model.NetworkContentResponse
import com.anshtya.movieinfo.common.data.network.model.WatchlistRequest
import com.anshtya.movieinfo.common.data.network.model.auth.DeleteSessionRequest
import com.anshtya.movieinfo.common.data.network.model.auth.LoginRequest
import com.anshtya.movieinfo.common.data.network.model.auth.LoginResponse
import com.anshtya.movieinfo.common.data.network.model.auth.NetworkAccountDetails
import com.anshtya.movieinfo.common.data.network.model.auth.RequestTokenResponse
import com.anshtya.movieinfo.common.data.network.model.auth.SessionRequest
import com.anshtya.movieinfo.common.data.network.model.auth.SessionResponse
import com.anshtya.movieinfo.common.data.network.model.details.NetworkMovieDetails
import com.anshtya.movieinfo.common.data.network.model.details.NetworkPersonDetails
import com.anshtya.movieinfo.common.data.network.model.details.tv.NetworkTvSeriesDetails
import com.anshtya.movieinfo.common.data.network.model.search.SearchResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpHeaders
import io.ktor.http.headers

internal class TmdbClient(
    private val httpClient: HttpClient
) {
    suspend fun getMovieLists(
        category: String,
        language: String = "en-US",
        page: Int,
        region: String? = null
    ): Result<NetworkContentResponse> {
        return runCatching {
            httpClient.get("movie/$category") {
                parameter("language", language)
                parameter("page", page)
                region?.let { parameter("region", it) }
            }.body<NetworkContentResponse>()
        }
    }

    suspend fun getTvShowLists(
        category: String,
        language: String = "en-US",
        page: Int
    ): Result<NetworkContentResponse> {
        return runCatching {
            httpClient.get("tv/$category") {
                parameter("language", language)
                parameter("page", page)
            }.body<NetworkContentResponse>()
        }
    }

    suspend fun multiSearch(
        page: Int = 1,
        query: String,
        includeAdult: Boolean
    ): Result<SearchResponse> {
        return runCatching {
            httpClient.get("search/multi") {
                parameter("page", page)
                parameter("query", query)
                parameter("include_adult", includeAdult)
            }.body<SearchResponse>()
        }
    }

    suspend fun getMovieDetails(
       id: Int,
       appendToResponse: String = "recommendations,credits"
    ): Result<NetworkMovieDetails> {
        return runCatching {
            httpClient.get("movie/$id") {
                parameter("append_to_response", appendToResponse)
            }.body<NetworkMovieDetails>()
        }
    }

    suspend fun getTvSeriesDetails(
        id: Int,
        appendToResponse: String = "recommendations,credits"
    ): Result<NetworkTvSeriesDetails> {
        return runCatching {
            httpClient.get("tv/$id") {
                parameter("append_to_response", appendToResponse)
            }.body<NetworkTvSeriesDetails>()
        }
    }

    suspend fun getPersonDetails(
        id: Int
    ): Result<NetworkPersonDetails> {
        return runCatching {
            httpClient.get("person/$id")
                .body<NetworkPersonDetails>()
        }
    }

    suspend fun getLibraryItems(
        accountId: Int,
        itemType: String,
        mediaType: String,
        page: Int
    ): Result<NetworkContentResponse> {
        return runCatching {
            httpClient.get("account/$accountId/$itemType/$mediaType") {
                parameter("page", page)
            }.body<NetworkContentResponse>()
        }
    }

    suspend fun addOrRemoveFavorite(
        accountId: Int,
        favoriteRequest: FavoriteRequest
    ): Result<Unit> {
        return runCatching {
            httpClient.post("account/$accountId/favorite") {
                headers {
                    append(HttpHeaders.ContentType, "application/json")
                }
                setBody(favoriteRequest)
            }
        }
    }

    suspend fun addOrRemoveFromWatchlist(
        accountId: Int,
        watchlistRequest: WatchlistRequest
    ): Result<Unit> {
        return runCatching {
            httpClient.post("account/$accountId/watchlist") {
                headers {
                    append(HttpHeaders.ContentType, "application/json")
                }
                setBody(watchlistRequest)
            }
        }
    }

    suspend fun createRequestToken(): Result<RequestTokenResponse> {
        return runCatching {
            httpClient.get("authentication/token/new")
                .body<RequestTokenResponse>()
        }
    }

    suspend fun validateWithLogin(
        loginRequest: LoginRequest
    ): Result<LoginResponse> {
        return runCatching {
            httpClient.post("authentication/token/validate_with_login") {
                headers {
                    append(HttpHeaders.ContentType, "application/json")
                }
                setBody(loginRequest)
            }.body<LoginResponse>()
        }
    }

    suspend fun createSession(
        sessionRequest: SessionRequest
    ): Result<SessionResponse> {
        return runCatching {
            httpClient.post("authentication/session/new") {
                headers {
                    append(HttpHeaders.ContentType, "application/json")
                }
                setBody(sessionRequest)
            }.body<SessionResponse>()
        }
    }

    suspend fun getAccountDetails(
        sessionId: String
    ): Result<NetworkAccountDetails> {
        return runCatching {
            httpClient.get("account") {
                parameter("session_id", sessionId)
            }.body<NetworkAccountDetails>()
        }
    }

    suspend fun getAccountDetailsWithId(
        accountId: Int
    ): Result<NetworkAccountDetails> {
        return runCatching {
            httpClient.get("account/$accountId")
                .body<NetworkAccountDetails>()
        }
    }

    suspend fun deleteSession(
        deleteSessionRequest: DeleteSessionRequest
    ): Result<Unit> {
        return runCatching {
            httpClient.delete("authentication/session") {
                headers {
                    append(HttpHeaders.ContentType, "application/json")
                }
                setBody(deleteSessionRequest)
            }
        }
    }
}