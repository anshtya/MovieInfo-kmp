package com.anshtya.movieinfo.common.data.workscheduler.di

import com.anshtya.movieinfo.common.data.util.ContextWrapper
import com.anshtya.movieinfo.common.data.workscheduler.IosWorkScheduler
import com.anshtya.movieinfo.common.data.workscheduler.WorkScheduler
import org.koin.core.annotation.Single

@Single
internal actual class WorkSchedulerProvider actual constructor(ctx: ContextWrapper) {
    @Single
    actual fun workScheduler(): WorkScheduler {
        return IosWorkScheduler()
    }
}