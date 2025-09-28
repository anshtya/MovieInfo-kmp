package com.anshtya.movieinfo.common.data.workscheduler.di

import androidx.work.WorkManager
import com.anshtya.movieinfo.common.data.util.ContextWrapper
import com.anshtya.movieinfo.common.data.workscheduler.AndroidWorkScheduler
import com.anshtya.movieinfo.common.data.workscheduler.WorkScheduler

actual class WorkSchedulerProvider actual constructor(val ctx: ContextWrapper) {
    actual fun workScheduler(): WorkScheduler {
        return AndroidWorkScheduler(
            workManager = WorkManager.getInstance(ctx.context.applicationContext)
        )
    }
}