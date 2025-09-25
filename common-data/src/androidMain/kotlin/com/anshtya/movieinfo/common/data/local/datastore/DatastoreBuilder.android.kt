package com.anshtya.movieinfo.common.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.anshtya.movieinfo.common.data.util.ContextWrapper
import okio.Path.Companion.toPath
import org.koin.core.annotation.Single

@Single
internal actual class DatastoreBuilder actual constructor(val ctx: ContextWrapper) {
    @Single
    actual fun preferencesDataStore(): DataStore<Preferences> {
        val context = ctx.context.applicationContext
        return PreferenceDataStoreFactory.createWithPath(
            produceFile = {
                context.filesDir.resolve(dataStoreFileName).absolutePath.toPath()
            }
        )
    }
}