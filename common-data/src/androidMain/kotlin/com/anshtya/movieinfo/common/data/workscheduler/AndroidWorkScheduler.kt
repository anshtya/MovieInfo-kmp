package com.anshtya.movieinfo.common.data.workscheduler

import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.anshtya.movieinfo.common.data.model.MediaType
import com.anshtya.movieinfo.common.data.workscheduler.util.FAVORITES_TAG
import com.anshtya.movieinfo.common.data.workscheduler.util.WATCHLIST_TAG
import com.anshtya.movieinfo.common.data.workscheduler.util.putEnum
import com.anshtya.movieinfo.common.data.workscheduler.worker.LibrarySyncWorker
import com.anshtya.movieinfo.common.data.workscheduler.worker.LibraryTaskWorker
import java.util.concurrent.TimeUnit

internal class AndroidWorkScheduler(
    private val workManager: WorkManager
): WorkScheduler {
    override fun scheduleLibraryTaskWork(
        libraryWork: LibraryWork
    ) {
        val libraryTaskWorkRequest = OneTimeWorkRequestBuilder<LibraryTaskWorker>()
            .setConstraints(getWorkConstraints())
            .setBackoffCriteria(
                BackoffPolicy.LINEAR,
                10L,
                TimeUnit.SECONDS
            )
            .setInputData(generateInputData(libraryWork))
            .build()

        workManager.enqueueUniqueWork(
            generateWorkerName(
                mediaId = libraryWork.mediaId,
                mediaType = libraryWork.mediaType,
                workType = libraryWork.workType
            ),
            ExistingWorkPolicy.REPLACE,
            libraryTaskWorkRequest
        )
    }

    override fun scheduleLibrarySyncWork() {
        val librarySyncWorkRequest = OneTimeWorkRequestBuilder<LibrarySyncWorker>()
            .setConstraints(getWorkConstraints())
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .build()

        workManager.enqueueUniqueWork(
            LibrarySyncWorker.Companion.SYNC_LIBRARY_WORK_NAME,
            ExistingWorkPolicy.REPLACE,
            librarySyncWorkRequest
        )
    }

    override fun isWorkNotScheduled(
        mediaId: Int,
        mediaType: MediaType,
        workType: LibraryWorkType
    ): Boolean {
        return workManager.getWorkInfosForUniqueWork(
            generateWorkerName(
                mediaId = mediaId,
                mediaType = mediaType,
                workType = workType
            )
        ).get()
            .any {
                it.state == WorkInfo.State.ENQUEUED
                        || it.state == WorkInfo.State.RUNNING
                        || it.state == WorkInfo.State.BLOCKED
            }
            .not()
    }

    private fun generateWorkerName(
        mediaId: Int,
        mediaType: MediaType,
        workType: LibraryWorkType
    ): String {
        return when (workType) {
            LibraryWorkType.FAVORITE -> "$FAVORITES_TAG-${mediaId}-${mediaType.name}"
            LibraryWorkType.WATCHLIST -> "$WATCHLIST_TAG-${mediaId}-${mediaType.name}"
        }
    }

    private fun generateInputData(libraryWork: LibraryWork) = Data.Builder()
        .putInt(LibraryTaskWorker.Companion.TASK_KEY, libraryWork.mediaId)
        .putEnum(LibraryTaskWorker.Companion.MEDIA_TYPE_KEY, libraryWork.mediaType)
        .putEnum(LibraryTaskWorker.Companion.ITEM_TYPE_KEY, libraryWork.workType)
        .putBoolean(LibraryTaskWorker.Companion.ITEM_EXISTS_KEY, libraryWork.itemExistLocally)
        .build()

    private fun getWorkConstraints() = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()
}