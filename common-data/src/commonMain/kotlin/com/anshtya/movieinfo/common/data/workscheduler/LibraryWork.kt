package com.anshtya.movieinfo.common.data.workscheduler

import com.anshtya.movieinfo.common.data.model.LibraryType
import com.anshtya.movieinfo.common.data.model.MediaType

/**
 * Class to encapsulate library work specifications.
 */
data class LibraryWork(
    val mediaId: Int,
    val mediaType: MediaType,
    val workType: LibraryType,
    val itemExistLocally: Boolean
) {
    companion object {
        fun favoriteItemWork(mediaId: Int, mediaType: MediaType, itemExists: Boolean) =
            LibraryWork(mediaId, mediaType, LibraryType.FAVORITE, itemExists)

        fun watchlistItemWork(mediaId: Int, mediaType: MediaType, itemExists: Boolean) =
            LibraryWork(mediaId, mediaType, LibraryType.WATCHLIST, itemExists)
    }
}