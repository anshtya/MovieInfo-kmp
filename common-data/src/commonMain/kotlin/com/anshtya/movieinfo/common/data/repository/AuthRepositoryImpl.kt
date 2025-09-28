package com.anshtya.movieinfo.common.data.repository

import com.anshtya.movieinfo.common.data.local.database.dao.AccountDetailsDao
import com.anshtya.movieinfo.common.data.local.database.dao.FavoriteContentDao
import com.anshtya.movieinfo.common.data.local.database.dao.WatchlistContentDao
import com.anshtya.movieinfo.common.data.local.datastore.preferences.UserPreferencesDataStore
import com.anshtya.movieinfo.common.data.local.datastore.session.SessionManager
import com.anshtya.movieinfo.common.data.network.TmdbClient
import com.anshtya.movieinfo.common.data.network.model.auth.DeleteSessionRequest
import com.anshtya.movieinfo.common.data.network.model.auth.LoginRequest
import com.anshtya.movieinfo.common.data.network.model.auth.SessionRequest
import com.anshtya.movieinfo.common.data.util.asEntity
import com.anshtya.movieinfo.common.data.workscheduler.WorkScheduler
import org.koin.core.annotation.Single

@Single
internal class AuthRepositoryImpl(
    private val tmdbClient: TmdbClient,
    private val favoriteContentDao: FavoriteContentDao,
    private val watchlistContentDao: WatchlistContentDao,
    private val accountDetailsDao: AccountDetailsDao,
    private val userPreferencesDataStore: UserPreferencesDataStore,
    private val sessionManager: SessionManager,
    private val workScheduler: WorkScheduler
) : AuthRepository {
    override val isLoggedIn = sessionManager.isLoggedIn

    override suspend fun login(
        username: String,
        password: String
    ): Result<Unit> {
        return runCatching {
            val response = tmdbClient.createRequestToken().getOrThrow()
            val loginRequest = LoginRequest(
                username = username,
                password = password,
                requestToken = response.requestToken
            )
            val loginResponse = tmdbClient.validateWithLogin(loginRequest).getOrThrow()

            val sessionRequest = SessionRequest(loginResponse.requestToken)
            val sessionResponse = tmdbClient.createSession(sessionRequest).getOrThrow()

            val accountDetails =
                tmdbClient.getAccountDetails(sessionResponse.sessionId)
                    .getOrThrow().asEntity()

            sessionManager.storeSessionId(sessionResponse.sessionId)
            accountDetailsDao.addAccountDetails(accountDetails)
            userPreferencesDataStore.setAdultResultPreference(accountDetails.includeAdult)

            workScheduler.scheduleLibrarySyncWork()
        }
    }

    override suspend fun logout(accountId: Int): Result<Unit> {
        return runCatching {
            val sessionId = sessionManager.getSessionId()!!
            val deleteSessionRequest = DeleteSessionRequest(sessionId)
            tmdbClient.deleteSession(deleteSessionRequest).getOrThrow()

            sessionManager.deleteSessionId()
            accountDetailsDao.deleteAccountDetails(accountId)
            favoriteContentDao.deleteAllFavoriteItems()
            watchlistContentDao.deleteAllWatchlistItems()
        }
    }
}