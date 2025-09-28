package com.anshtya.movieinfo.common.data.workscheduler.di

import com.anshtya.movieinfo.common.data.util.ContextWrapper
import com.anshtya.movieinfo.common.data.workscheduler.WorkScheduler

expect class WorkSchedulerProvider(ctx: ContextWrapper) {
    fun workScheduler(): WorkScheduler
}