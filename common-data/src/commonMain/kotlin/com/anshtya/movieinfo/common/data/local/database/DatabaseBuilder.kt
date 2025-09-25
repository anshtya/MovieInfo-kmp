package com.anshtya.movieinfo.common.data.local.database

import androidx.room.RoomDatabase
import com.anshtya.movieinfo.common.data.util.ContextWrapper
import org.koin.core.annotation.Single

@Single
internal expect class DatabaseBuilder(ctx: ContextWrapper) {
    @Single
    fun builder(): RoomDatabase.Builder<MovieInfoDatabase>
}