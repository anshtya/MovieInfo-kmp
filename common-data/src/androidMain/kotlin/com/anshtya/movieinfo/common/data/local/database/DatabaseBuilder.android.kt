package com.anshtya.movieinfo.common.data.local.database

import androidx.room.Room
import androidx.room.RoomDatabase
import com.anshtya.movieinfo.common.data.util.ContextWrapper
import org.koin.core.annotation.Single

@Single
internal actual class DatabaseBuilder actual constructor(val ctx: ContextWrapper) {
    @Single
    actual fun builder(): RoomDatabase.Builder<MovieInfoDatabase> {
        val appContext = ctx.context.applicationContext
        val dbFile = appContext.getDatabasePath(databaseName)
        return Room.databaseBuilder<MovieInfoDatabase>(
            context = appContext,
            name = dbFile.absolutePath
        )
    }
}