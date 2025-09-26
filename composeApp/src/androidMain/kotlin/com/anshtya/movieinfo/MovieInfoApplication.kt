package com.anshtya.movieinfo

import android.app.Application
import com.anshtya.movieinfo.di.AppModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.ksp.generated.module

class MovieInfoApplication : Application(), KoinComponent {

//    val workerFactory: KoinWorkerFactory by inject()
//
//    override val workManagerConfiguration: Configuration
//        get() = Configuration.Builder()
//            .apply {
//                setWorkerFactory(workerFactory)
////                if (BuildConfig.DEBUG) {
////                    setMinimumLoggingLevel(android.util.Log.DEBUG)
////                } else {
////                    setMinimumLoggingLevel(android.util.Log.ERROR)
////                }
//            }
//            .build()

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@MovieInfoApplication)
            workManagerFactory()
            modules(AppModule().module)
        }
    }
}