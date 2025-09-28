package com.anshtya.movieinfo.common.data.di

import com.anshtya.movieinfo.common.data.local.di.DatabaseModule
import com.anshtya.movieinfo.common.data.local.di.DatastoreModule
import com.anshtya.movieinfo.common.data.network.di.HttpClientModule
import com.anshtya.movieinfo.common.data.workscheduler.di.WorkSchedulerModule
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module(
    includes = [
        DatabaseModule::class,
        DatastoreModule::class,
        HttpClientModule::class,
        WorkSchedulerModule::class,
    ]
)
@ComponentScan("com.anshtya.movieinfo.common.data")
class CommonDataModule