package com.anshtya.movieinfo.common.data.local.database

import androidx.room.RoomDatabase
import com.anshtya.movieinfo.common.data.util.ContextWrapper

internal expect fun databaseBuilder(
    ctx: ContextWrapper
): RoomDatabase.Builder<MovieInfoDatabase>