package com.anshtya.movieinfo.common.data.workscheduler

import com.anshtya.movieinfo.common.data.model.LibraryItem
import com.anshtya.movieinfo.common.data.model.MediaType
import com.anshtya.movieinfo.common.data.network.model.NetworkContent
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

/**
 * Class to provide a common implementation for synchronization of user library
 * between local and remote data source.
 */
abstract class LibrarySynchronizer {
    protected suspend fun syncFromLocalAndNetwork(
        fetchFromNetwork: suspend (
            mediaTypeString: String
        ) -> List<NetworkContent>,

        fetchStaleItemsFromLocalSource: suspend (
            mediaType: MediaType,
            networkResultsPair: List<Pair<Int, String>>
        ) -> List<Pair<Int, String>>,

        fetchFromLocalSource: suspend (
            mediaType: MediaType,
            mediaTypeString: String,
            networkResults: List<NetworkContent>
        ) -> List<LibraryItem>,

        updateLocalSource: suspend (
            libraryItems: List<LibraryItem>,
            staleItems: List<Pair<Int, String>>
        ) -> Unit
    ): Boolean {
        val movieMediaTypeString = MediaType.MOVIE.name.lowercase()
        val tvMediaTypeString = MediaType.TV.name.lowercase()

        val moviesLibrary = mutableListOf<LibraryItem>()
        val tvShowsLibrary = mutableListOf<LibraryItem>()
        val staleMovies = mutableListOf<Pair<Int, String>>()
        val staleTvShows = mutableListOf<Pair<Int, String>>()

        val syncResult = runCatching {
            coroutineScope {
                launch {
                    val moviesLibraryNetworkResults = mutableListOf<NetworkContent>()

                    moviesLibraryNetworkResults.addAll(
                        fetchFromNetwork("${movieMediaTypeString}s")
                    )

                    val moviesLibraryNetworkResultsPair = moviesLibraryNetworkResults
                        .map {
                            Pair(it.id, movieMediaTypeString)
                        }

                    staleMovies.addAll(
                        fetchStaleItemsFromLocalSource(
                            MediaType.MOVIE, moviesLibraryNetworkResultsPair
                        )
                    )

                    moviesLibrary.addAll(
                        fetchFromLocalSource(
                            MediaType.MOVIE, movieMediaTypeString, moviesLibraryNetworkResults
                        )
                    )
                }

                launch {
                    val tvShowsLibraryNetworkResults = mutableListOf<NetworkContent>()

                    tvShowsLibraryNetworkResults.addAll(
                        fetchFromNetwork(tvMediaTypeString)
                    )

                    val tvShowsLibraryNetworkResultsPair = tvShowsLibraryNetworkResults
                        .map {
                            Pair(it.id, tvMediaTypeString)
                        }

                    staleTvShows.addAll(
                        fetchStaleItemsFromLocalSource(
                            MediaType.TV, tvShowsLibraryNetworkResultsPair
                        )
                    )

                    tvShowsLibrary.addAll(
                        fetchFromLocalSource(
                            MediaType.TV, tvMediaTypeString, tvShowsLibraryNetworkResults
                        )
                    )
                }
            }

            updateLocalSource(
                (moviesLibrary + tvShowsLibrary),
                (staleMovies + staleTvShows)
            )
        }

        return syncResult.isSuccess
    }
}