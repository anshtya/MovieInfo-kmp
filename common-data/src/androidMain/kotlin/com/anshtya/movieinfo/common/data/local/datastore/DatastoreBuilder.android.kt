package com.anshtya.movieinfo.common.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.anshtya.movieinfo.common.data.util.ContextWrapper
import okio.Path.Companion.toPath

internal actual class DatastoreBuilder actual constructor(val ctx: ContextWrapper) {
    actual fun preferencesDataStore(): DataStore<Preferences> {
        val context = ctx.context.applicationContext
        return PreferenceDataStoreFactory.createWithPath(
            produceFile = {
                context.filesDir.resolve(dataStoreFileName).absolutePath.toPath()
            }
        )
    }
}