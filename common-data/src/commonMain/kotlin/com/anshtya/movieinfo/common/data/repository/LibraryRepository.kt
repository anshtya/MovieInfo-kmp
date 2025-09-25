package com.anshtya.movieinfo.common.data.repository

import com.anshtya.movieinfo.common.data.model.LibraryItem
import com.anshtya.movieinfo.common.data.model.MediaType
import com.anshtya.movieinfo.common.data.workscheduler.LibrarySync
import com.anshtya.movieinfo.common.data.workscheduler.LibraryWorkOperation
import kotlinx.coroutines.flow.Flow

interface LibraryRepository: LibraryWorkOperation, LibrarySync {
    val favoriteMovies: Flow<List<LibraryItem>>
    val favoriteTvShows: Flow<List<LibraryItem>>
    val moviesWatchlist: Flow<List<LibraryItem>>
    val tvShowsWatchlist: Flow<List<LibraryItem>>

    suspend fun itemInFavoritesExists(
        mediaId: Int,
        mediaType: MediaType
    ): Boolean

    suspend fun itemInWatchlistExists(
        mediaId: Int,
        mediaType: MediaType
    ): Boolean

    suspend fun addOrRemoveFavorite(libraryItem: LibraryItem)

    suspend fun addOrRemoveFromWatchlist(libraryItem: LibraryItem)
}