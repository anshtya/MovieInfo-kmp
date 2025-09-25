package com.anshtya.movieinfo.common.data.workscheduler.di

import com.anshtya.movieinfo.common.data.workscheduler.WorkScheduler
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@ComponentScan
internal class WorkSchedulerModule {
    @Single
    fun workScheduler(
        provider: WorkSchedulerProvider
    ): WorkScheduler {
        return provider.workScheduler()
    }
}