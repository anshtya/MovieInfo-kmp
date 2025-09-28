package com.anshtya.movieinfo.common.data.local.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.anshtya.movieinfo.common.data.local.database.MovieInfoDatabase
import com.anshtya.movieinfo.common.data.local.database.MovieInfoDatabase.Companion.MIGRATION_10_11
import com.anshtya.movieinfo.common.data.local.database.MovieInfoDatabase.Companion.MIGRATION_11_12
import com.anshtya.movieinfo.common.data.local.database.MovieInfoDatabase.Companion.MIGRATION_1_2
import com.anshtya.movieinfo.common.data.local.database.MovieInfoDatabase.Companion.MIGRATION_2_3
import com.anshtya.movieinfo.common.data.local.database.MovieInfoDatabase.Companion.MIGRATION_3_4
import com.anshtya.movieinfo.common.data.local.database.MovieInfoDatabase.Companion.MIGRATION_4_5
import com.anshtya.movieinfo.common.data.local.database.MovieInfoDatabase.Companion.MIGRATION_5_6
import com.anshtya.movieinfo.common.data.local.database.MovieInfoDatabase.Companion.MIGRATION_6_7
import com.anshtya.movieinfo.common.data.local.database.MovieInfoDatabase.Companion.MIGRATION_7_8
import com.anshtya.movieinfo.common.data.local.database.MovieInfoDatabase.Companion.MIGRATION_8_9
import com.anshtya.movieinfo.common.data.local.database.MovieInfoDatabase.Companion.MIGRATION_9_10
import com.anshtya.movieinfo.common.data.local.database.dao.AccountDetailsDao
import com.anshtya.movieinfo.common.data.local.database.dao.FavoriteContentDao
import com.anshtya.movieinfo.common.data.local.database.dao.WatchlistContentDao
import com.anshtya.movieinfo.common.data.local.database.databaseBuilder
import com.anshtya.movieinfo.common.data.util.ContextModule
import com.anshtya.movieinfo.common.data.util.ContextWrapper
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module(
    includes = [ContextModule::class]
)
internal class DatabaseModule {
    @Single
    fun provideMovieInfoDatabase(
        ctx: ContextWrapper
    ): MovieInfoDatabase {
        return databaseBuilder(ctx)
            .addMigrations(
                MIGRATION_1_2,
                MIGRATION_2_3,
                MIGRATION_3_4,
                MIGRATION_4_5,
                MIGRATION_5_6,
                MIGRATION_6_7,
                MIGRATION_7_8,
                MIGRATION_8_9,
                MIGRATION_9_10,
                MIGRATION_10_11,
                MIGRATION_11_12
            )
            .setDriver(BundledSQLiteDriver())
            .build()
    }

    @Single
    fun provideFavoriteContentDao(
        db: MovieInfoDatabase
    ): FavoriteContentDao {
        return db.favoriteContentDao()
    }

    @Single
    fun provideWatchlistContentDao(
        db: MovieInfoDatabase
    ): WatchlistContentDao {
        return db.watchlistContentDao()
    }

    @Single
    fun provideAccountDetailsDao(
        db: MovieInfoDatabase
    ): AccountDetailsDao {
        return db.accountDetailsDao()
    }
}