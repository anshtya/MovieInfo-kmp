package com.anshtya.movieinfo.common.data.workscheduler

import com.anshtya.movieinfo.common.data.model.MediaType

/**
 * Interface to add capability to execute operation of [LibraryWork] work enqueued by
 * [WorkScheduler].
 */
internal interface LibraryWorkExecutor {
    suspend fun executeLibraryWork(
        id: Int,
        mediaType: MediaType,
        libraryWorkType: LibraryWorkType,
        itemExistsLocally: Boolean
    ): Boolean
}