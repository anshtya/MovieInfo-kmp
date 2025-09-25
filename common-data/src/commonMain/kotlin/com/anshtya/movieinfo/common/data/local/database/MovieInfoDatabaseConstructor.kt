package com.anshtya.movieinfo.common.data.local.database

import androidx.room.RoomDatabaseConstructor

@Suppress("KotlinNoActualForExpect")
internal expect object MovieInfoDatabaseConstructor : RoomDatabaseConstructor<MovieInfoDatabase> {
    override fun initialize(): MovieInfoDatabase
}