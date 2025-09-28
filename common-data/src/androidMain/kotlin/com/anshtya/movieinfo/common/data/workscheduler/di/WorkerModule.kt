package com.anshtya.movieinfo.common.data.workscheduler.di

import com.anshtya.movieinfo.common.data.workscheduler.worker.LibrarySyncWorker
import com.anshtya.movieinfo.common.data.workscheduler.worker.LibraryTaskWorker
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.module

val workerModule = module {
    worker { LibrarySyncWorker(get(), get(), get()) }
    worker { LibraryTaskWorker(get(), get(), get()) }
}