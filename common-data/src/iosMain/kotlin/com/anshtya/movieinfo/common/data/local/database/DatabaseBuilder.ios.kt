package com.anshtya.movieinfo.common.data.local.database

import androidx.room.Room
import androidx.room.RoomDatabase
import com.anshtya.movieinfo.common.data.util.ContextWrapper
import kotlinx.cinterop.ExperimentalForeignApi
import org.koin.core.annotation.Single
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

@Single
internal actual class DatabaseBuilder actual constructor(val ctx: ContextWrapper) {
    @Single
    actual fun builder(): RoomDatabase.Builder<MovieInfoDatabase> {
        val dbFilePath = documentDirectory() + "/$databaseName"
        return Room.databaseBuilder<MovieInfoDatabase>(
            name = dbFilePath,
        )
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun documentDirectory(): String {
    val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )
    return requireNotNull(documentDirectory?.path)
}