package com.anshtya.movieinfo.common.data.workscheduler

import com.anshtya.movieinfo.common.data.model.MediaType

interface WorkScheduler {
    fun scheduleLibraryTaskWork(
        libraryWork: LibraryWork
    )

    fun scheduleLibrarySyncWork()

    fun isWorkNotScheduled(
        mediaId: Int,
        mediaType: MediaType,
        workType: LibraryWorkType
    ): Boolean
}