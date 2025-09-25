package com.anshtya.movieinfo.common.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.anshtya.movieinfo.common.data.util.ContextWrapper
import org.koin.core.annotation.Single

@Single
internal expect class DatastoreBuilder(ctx: ContextWrapper) {
    @Single
    fun preferencesDataStore(): DataStore<Preferences>
}

internal const val dataStoreFileName = "user_prefs.preferences_pb"