package com.anshtya.movieinfo.common.data.workscheduler.di

import com.anshtya.movieinfo.common.data.util.ContextWrapper
import com.anshtya.movieinfo.common.data.workscheduler.WorkScheduler
import org.koin.core.annotation.Single

@Single
internal expect class WorkSchedulerProvider(ctx: ContextWrapper) {
    @Single
    fun workScheduler(): WorkScheduler
}