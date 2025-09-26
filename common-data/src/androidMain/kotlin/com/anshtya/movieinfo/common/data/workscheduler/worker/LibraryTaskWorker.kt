package com.anshtya.movieinfo.common.data.workscheduler.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.anshtya.movieinfo.common.data.model.MediaType
import com.anshtya.movieinfo.common.data.workscheduler.LibraryWorkExecutor
import com.anshtya.movieinfo.common.data.workscheduler.LibraryWorkType
import com.anshtya.movieinfo.common.data.workscheduler.util.getEnum
import org.koin.android.annotation.KoinWorker
import org.koin.core.annotation.InjectedParam

@KoinWorker
internal class LibraryTaskWorker(
    @InjectedParam private val appContext: Context,
    @InjectedParam workerParams: WorkerParameters,
    private val libraryWorkExecutor: LibraryWorkExecutor
) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        val itemId = inputData.getInt(TASK_KEY, 0)
        val mediaType = inputData.getEnum<MediaType>(key = MEDIA_TYPE_KEY)
        val taskType = inputData.getEnum<LibraryWorkType>(ITEM_TYPE_KEY)
        val itemExists = inputData.getBoolean(ITEM_EXISTS_KEY, false)

        val syncSuccessful = libraryWorkExecutor.executeLibraryWork(
            id = itemId,
            mediaType = mediaType,
            libraryWorkType = taskType,
            itemExistsLocally = itemExists
        )

        return if (syncSuccessful) {
            Result.success()
        } else {
            Result.retry()
        }
    }

    companion object {
        const val TASK_KEY = "task_key"
        const val MEDIA_TYPE_KEY = "media_type_key"
        const val ITEM_TYPE_KEY = "item_type_key"
        const val ITEM_EXISTS_KEY = "item_exists_key"
    }
}