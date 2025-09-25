package com.anshtya.movieinfo.common.data.repository.test.data

import com.anshtya.movieinfo.common.data.model.LibraryItem
import com.anshtya.movieinfo.common.data.model.MediaType

internal val tvMediaType = MediaType.TV.name.lowercase()
internal val movieMediaType = MediaType.MOVIE.name.lowercase()

val testLibraryItems: List<LibraryItem> = listOf(
    LibraryItem(
        id = 1,
        imagePath = "path",
        name = "name",
        mediaType = movieMediaType
    ),
    LibraryItem(
        id = 2,
        imagePath = "path",
        name = "name",
        mediaType = tvMediaType
    )
)