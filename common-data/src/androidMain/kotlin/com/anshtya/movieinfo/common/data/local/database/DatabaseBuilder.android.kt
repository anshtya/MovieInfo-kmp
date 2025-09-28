package com.anshtya.movieinfo.common.data.local.database

import androidx.room.Room
import androidx.room.RoomDatabase
import com.anshtya.movieinfo.common.data.util.ContextWrapper

internal actual fun databaseBuilder(
    ctx: ContextWrapper
): RoomDatabase.Builder<MovieInfoDatabase> {
    val appContext = ctx.context.applicationContext
    val dbFile = appContext.getDatabasePath(databaseName)
    return Room.databaseBuilder<MovieInfoDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
}