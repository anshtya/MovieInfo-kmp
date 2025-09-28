package com.anshtya.movieinfo.common.data.workscheduler.di

import com.anshtya.movieinfo.common.data.util.ContextWrapper
import com.anshtya.movieinfo.common.data.workscheduler.IosWorkScheduler
import com.anshtya.movieinfo.common.data.workscheduler.WorkScheduler

actual class WorkSchedulerProvider actual constructor(ctx: ContextWrapper) {
    actual fun workScheduler(): WorkScheduler {
        return IosWorkScheduler()
    }
}