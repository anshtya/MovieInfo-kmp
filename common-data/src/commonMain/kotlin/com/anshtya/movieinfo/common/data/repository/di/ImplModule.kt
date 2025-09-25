package com.anshtya.movieinfo.common.data.repository.di

import com.anshtya.movieinfo.common.data.local.database.dao.AccountDetailsDao
import com.anshtya.movieinfo.common.data.local.database.dao.FavoriteContentDao
import com.anshtya.movieinfo.common.data.local.database.dao.WatchlistContentDao
import com.anshtya.movieinfo.common.data.local.datastore.preferences.UserPreferencesDataStore
import com.anshtya.movieinfo.common.data.local.datastore.session.SessionManager
import com.anshtya.movieinfo.common.data.network.TmdbClient
import com.anshtya.movieinfo.common.data.repository.AuthRepositoryImpl
import com.anshtya.movieinfo.common.data.repository.ContentRepositoryImpl
import com.anshtya.movieinfo.common.data.repository.DetailsRepositoryImpl
import com.anshtya.movieinfo.common.data.repository.LibraryRepositoryImpl
import com.anshtya.movieinfo.common.data.repository.SearchRepositoryImpl
import com.anshtya.movieinfo.common.data.repository.UserRepositoryImpl
import com.anshtya.movieinfo.common.data.workscheduler.WorkScheduler
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@ComponentScan("com.anshtya.movieinfo.common.data")
internal class ImplModule {
    @Single
    fun authRepositoryImpl(
        tmdbClient: TmdbClient,
        favoriteContentDao: FavoriteContentDao,
        watchlistContentDao: WatchlistContentDao,
        accountDetailsDao: AccountDetailsDao,
        userPreferencesDataStore: UserPreferencesDataStore,
        sessionManager: SessionManager,
        workScheduler: WorkScheduler
    ): AuthRepositoryImpl {
        return AuthRepositoryImpl(
            tmdbClient = tmdbClient,
            favoriteContentDao = favoriteContentDao,
            watchlistContentDao = watchlistContentDao,
            accountDetailsDao = accountDetailsDao,
            userPreferencesDataStore = userPreferencesDataStore,
            sessionManager = sessionManager,
            workScheduler = workScheduler
        )
    }

    @Single
    fun contentRepositoryImpl(
        tmdbClient: TmdbClient,
        accountDetailsDao: AccountDetailsDao
    ): ContentRepositoryImpl {
        return ContentRepositoryImpl(
            tmdbClient = tmdbClient,
            accountDetailsDao = accountDetailsDao
        )
    }

    @Single
    fun detailsRepositoryImpl(
        tmdbClient: TmdbClient
    ): DetailsRepositoryImpl {
        return DetailsRepositoryImpl(
            tmdbClient = tmdbClient
        )
    }

    @Single
    fun libraryRepositoryImpl(
        tmdbClient: TmdbClient,
        favoriteContentDao: FavoriteContentDao,
        watchlistContentDao: WatchlistContentDao,
        accountDetailsDao: AccountDetailsDao,
        workScheduler: WorkScheduler
    ): LibraryRepositoryImpl {
        return LibraryRepositoryImpl(
            tmdbClient = tmdbClient,
            favoriteContentDao = favoriteContentDao,
            watchlistContentDao = watchlistContentDao,
            accountDetailsDao = accountDetailsDao,
            workScheduler = workScheduler
        )
    }

    @Single
    fun searchRepositoryImpl(
        tmdbClient: TmdbClient
    ): SearchRepositoryImpl {
        return SearchRepositoryImpl(
            tmdbClient = tmdbClient
        )
    }

    @Single
    fun userRepositoryImpl(
        tmdbClient: TmdbClient,
        accountDetailsDao: AccountDetailsDao,
        userPreferencesDataStore: UserPreferencesDataStore,
    ): UserRepositoryImpl {
        return UserRepositoryImpl(
            tmdbClient = tmdbClient,
            accountDetailsDao = accountDetailsDao,
            userPreferencesDataStore = userPreferencesDataStore,
        )
    }
}