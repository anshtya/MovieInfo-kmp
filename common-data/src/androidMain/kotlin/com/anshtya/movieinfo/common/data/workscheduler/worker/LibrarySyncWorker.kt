package com.anshtya.movieinfo.common.data.workscheduler.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.anshtya.movieinfo.common.data.repository.LibraryRepository
import com.anshtya.movieinfo.common.data.workscheduler.util.SYNC_NOTIFICATION_ID
import com.anshtya.movieinfo.common.data.workscheduler.util.workNotification
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.koin.android.annotation.KoinWorker
import org.koin.core.annotation.InjectedParam

@KoinWorker
class LibrarySyncWorker(
    @InjectedParam private val appContext: Context,
    @InjectedParam workerParams: WorkerParameters,
    private val libraryRepository: LibraryRepository,
) : CoroutineWorker(appContext, workerParams) {
    override suspend fun getForegroundInfo(): ForegroundInfo {
        return ForegroundInfo(SYNC_NOTIFICATION_ID, appContext.workNotification())
    }

    override suspend fun doWork(): Result = coroutineScope {
        val syncFavorites = async { libraryRepository.syncFavorites() }
        val syncWatchList = async { libraryRepository.syncWatchlist() }
        val syncSuccessful = awaitAll(syncFavorites, syncWatchList).all { it }

        return@coroutineScope if (syncSuccessful) {
            Result.success()
        } else {
            Result.retry()
        }
    }

    companion object {
        const val SYNC_LIBRARY_WORK_NAME = "sync_library"
    }
}