package com.anshtya.movieinfo.common.data.local.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.anshtya.movieinfo.common.data.local.datastore.DatastoreBuilder
import com.anshtya.movieinfo.common.data.local.datastore.preferences.UserPreferencesDataStore
import com.anshtya.movieinfo.common.data.local.datastore.session.SessionManager
import com.anshtya.movieinfo.common.data.util.ContextModule
import com.anshtya.movieinfo.common.data.util.ContextWrapper
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module(
    includes = [ContextModule::class]
)
internal class DatastoreModule {
    @Single
    fun dataStorePreferences(
        ctx: ContextWrapper
    ): DataStore<Preferences> {
        return DatastoreBuilder(ctx).preferencesDataStore()
    }

    @Single
    fun userPreferencesDataStore(
        preferencesDatastore: DataStore<Preferences>
    ): UserPreferencesDataStore {
        return UserPreferencesDataStore(
            datastore = preferencesDatastore
        )
    }

    @Single
    fun sessionManager(
        preferencesDatastore: DataStore<Preferences>
    ): SessionManager {
        return SessionManager(
            datastore = preferencesDatastore
        )
    }
}