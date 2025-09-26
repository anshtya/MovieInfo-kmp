package com.anshtya.movieinfo.common.data.workscheduler

import com.anshtya.movieinfo.common.data.model.LibraryType
import com.anshtya.movieinfo.common.data.model.MediaType

internal class IosWorkScheduler(): WorkScheduler {
    override fun scheduleLibraryTaskWork(
        libraryWork: LibraryWork
    ) {
        println("Not yet implemented")
    }

    override fun scheduleLibrarySyncWork() {
        println("Not yet implemented")
    }

    override fun isWorkNotScheduled(
        mediaId: Int,
        mediaType: MediaType,
        workType: LibraryType
    ): Boolean {
        println("Not yet implemented")
        return true
    }
}