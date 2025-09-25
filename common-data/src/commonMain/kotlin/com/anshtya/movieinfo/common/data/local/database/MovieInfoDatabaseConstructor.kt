package com.anshtya.movieinfo.common.data.local.database

import androidx.room.RoomDatabaseConstructor

@Suppress("KotlinNoActualForExpect")
expect object MovieInfoDatabaseConstructor : RoomDatabaseConstructor<MovieInfoDatabase> {
    override fun initialize(): MovieInfoDatabase
}