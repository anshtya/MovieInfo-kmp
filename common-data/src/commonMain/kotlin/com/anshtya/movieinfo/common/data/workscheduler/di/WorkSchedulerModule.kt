package com.anshtya.movieinfo.common.data.workscheduler.di

import com.anshtya.movieinfo.common.data.util.ContextModule
import com.anshtya.movieinfo.common.data.util.ContextWrapper
import com.anshtya.movieinfo.common.data.workscheduler.WorkScheduler
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module(
    includes = [ContextModule::class]
)
class WorkSchedulerModule {
    @Single
    fun workScheduler(
        ctx: ContextWrapper
    ): WorkScheduler {
        return WorkSchedulerProvider(ctx).workScheduler()
    }
}