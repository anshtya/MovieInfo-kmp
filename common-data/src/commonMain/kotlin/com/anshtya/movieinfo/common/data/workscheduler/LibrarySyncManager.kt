package com.anshtya.movieinfo.common.data.workscheduler

/**
 * Interface to add user library sync capabilities to a class to manage synchronization
 * between local and remote data source.
 */
internal interface LibrarySyncManager {
    suspend fun syncFavorites(): Boolean

    suspend fun syncWatchlist(): Boolean
}