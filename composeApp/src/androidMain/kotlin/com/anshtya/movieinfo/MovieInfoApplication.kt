package com.anshtya.movieinfo

import android.app.Application
import com.anshtya.movieinfo.common.data.workscheduler.di.workerModule
import com.anshtya.movieinfo.di.AppModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.ksp.generated.module

class MovieInfoApplication : Application(), KoinComponent {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@MovieInfoApplication)
            modules(
                AppModule().module,
                workerModule
            )
            workManagerFactory()
        }
    }
}