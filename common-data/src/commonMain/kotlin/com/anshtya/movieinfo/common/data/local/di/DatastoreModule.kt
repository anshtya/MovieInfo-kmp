package com.anshtya.movieinfo.common.data.local.di

import com.anshtya.movieinfo.common.data.local.datastore.DatastoreBuilder
import com.anshtya.movieinfo.common.data.local.datastore.preferences.UserPreferencesDataStore
import com.anshtya.movieinfo.common.data.local.datastore.session.SessionManager
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@ComponentScan("com.anshtya.movieinfo.common.data.local.datastore")
internal class DatastoreModule {
    @Single
    fun userPreferencesDataStore(
        builder: DatastoreBuilder
    ): UserPreferencesDataStore {
        return UserPreferencesDataStore(
            datastore = builder.preferencesDataStore()
        )
    }

    @Single
    fun sessionManager(
        builder: DatastoreBuilder
    ): SessionManager {
        return SessionManager(
            datastore = builder.preferencesDataStore()
        )
    }
}