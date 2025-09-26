package com.anshtya.movieinfo.common.data.di

import com.anshtya.movieinfo.common.data.repository.di.RepositoryModule
import com.anshtya.movieinfo.common.data.workscheduler.di.WorkSchedulerModule
import org.koin.core.annotation.Module

@Module(
    includes = [
        RepositoryModule::class,
        WorkSchedulerModule::class,
    ]
)
class CommonDataModule