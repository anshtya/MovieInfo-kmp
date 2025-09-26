package com.anshtya.movieinfo.common.data.repository

import com.anshtya.movieinfo.common.data.local.database.dao.AccountDetailsDao
import com.anshtya.movieinfo.common.data.local.database.dao.FavoriteContentDao
import com.anshtya.movieinfo.common.data.local.database.dao.WatchlistContentDao
import com.anshtya.movieinfo.common.data.local.database.entity.asFavoriteContentEntity
import com.anshtya.movieinfo.common.data.local.database.entity.asLibraryItem
import com.anshtya.movieinfo.common.data.local.database.entity.asWatchlistContentEntity
import com.anshtya.movieinfo.common.data.model.LibraryItem
import com.anshtya.movieinfo.common.data.model.LibraryType
import com.anshtya.movieinfo.common.data.model.MediaType
import com.anshtya.movieinfo.common.data.network.TmdbClient
import com.anshtya.movieinfo.common.data.network.model.FavoriteRequest
import com.anshtya.movieinfo.common.data.network.model.NetworkContent
import com.anshtya.movieinfo.common.data.network.model.WatchlistRequest
import com.anshtya.movieinfo.common.data.network.model.asModel
import com.anshtya.movieinfo.common.data.workscheduler.LibrarySyncManager
import com.anshtya.movieinfo.common.data.workscheduler.LibrarySynchronizer
import com.anshtya.movieinfo.common.data.workscheduler.LibraryWork
import com.anshtya.movieinfo.common.data.workscheduler.LibraryWorkExecutor
import com.anshtya.movieinfo.common.data.workscheduler.WorkScheduler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

internal class LibraryRepositoryImpl(
    private val tmdbClient: TmdbClient,
    private val favoriteContentDao: FavoriteContentDao,
    private val watchlistContentDao: WatchlistContentDao,
    private val accountDetailsDao: AccountDetailsDao,
    private val workScheduler: WorkScheduler
) : LibraryRepository, LibraryWorkExecutor, LibrarySyncManager, LibrarySynchronizer() {
    override val favoriteMovies: Flow<List<LibraryItem>> =
        favoriteContentDao.getFavoriteMovies().map {
            it.map { entity -> entity.asLibraryItem() }
        }

    override val favoriteTvShows: Flow<List<LibraryItem>> =
        favoriteContentDao.getFavoriteTvShows().map {
            it.map { entity -> entity.asLibraryItem() }
        }

    override val moviesWatchlist: Flow<List<LibraryItem>> =
        watchlistContentDao.getMoviesWatchlist().map {
            it.map { entity -> entity.asLibraryItem() }
        }

    override val tvShowsWatchlist: Flow<List<LibraryItem>> =
        watchlistContentDao.getTvShowsWatchlist().map {
            it.map { entity -> entity.asLibraryItem() }
        }

    override suspend fun itemInFavoritesExists(
        mediaId: Int,
        mediaType: MediaType
    ): Boolean {
        return favoriteContentDao.checkFavoriteItemExists(
            mediaId = mediaId,
            mediaType = mediaType.name.lowercase()
        )
    }

    override suspend fun itemInWatchlistExists(
        mediaId: Int,
        mediaType: MediaType
    ): Boolean {
        return watchlistContentDao.checkWatchlistItemExists(
            mediaId = mediaId,
            mediaType = mediaType.name.lowercase()
        )
    }

    override suspend fun addOrRemoveFavorite(
        libraryItem: LibraryItem
    ): Result<Unit> {
        return runCatching {
            val itemExists = favoriteContentDao.checkFavoriteItemExists(
                mediaId = libraryItem.id,
                mediaType = libraryItem.mediaType
            )
            if (itemExists) {
                favoriteContentDao.deleteFavoriteItem(
                    mediaId = libraryItem.id,
                    mediaType = libraryItem.mediaType
                )
            } else {
                favoriteContentDao.insertFavoriteItem(libraryItem.asFavoriteContentEntity())
            }

            val libraryWork = LibraryWork.favoriteItemWork(
                mediaId = libraryItem.id,
                mediaType = enumValueOf<MediaType>(libraryItem.mediaType.uppercase()),
                itemExists = !itemExists
            )
            workScheduler.scheduleLibraryTaskWork(libraryWork)
        }
    }

    override suspend fun addOrRemoveFromWatchlist(
        libraryItem: LibraryItem
    ): Result<Unit> {
        return runCatching {
            val itemExists = watchlistContentDao.checkWatchlistItemExists(
                mediaId = libraryItem.id,
                mediaType = libraryItem.mediaType
            )
            if (itemExists) {
                watchlistContentDao.deleteWatchlistItem(
                    mediaId = libraryItem.id,
                    mediaType = libraryItem.mediaType
                )
            } else {
                watchlistContentDao.insertWatchlistItem(libraryItem.asWatchlistContentEntity())
            }

            val libraryWork = LibraryWork.watchlistItemWork(
                mediaId = libraryItem.id,
                mediaType = enumValueOf<MediaType>(libraryItem.mediaType.uppercase()),
                itemExists = !itemExists
            )
            workScheduler.scheduleLibraryTaskWork(libraryWork)
        }
    }

    override suspend fun executeLibraryWork(
        id: Int,
        mediaType: MediaType,
        libraryType: LibraryType,
        itemExistsLocally: Boolean
    ): Boolean {
        val accountId = accountDetailsDao.getAccountDetails()?.id ?: return false
        val libraryWork = when (libraryType) {
            LibraryType.FAVORITE -> {
                val favoriteRequest = FavoriteRequest(
                    mediaType = mediaType.name.lowercase(),
                    mediaId = id,
                    favorite = itemExistsLocally
                )
                tmdbClient.addOrRemoveFavorite(accountId, favoriteRequest)
            }

            LibraryType.WATCHLIST -> {
                val watchlistRequest = WatchlistRequest(
                    mediaType = mediaType.name.lowercase(),
                    mediaId = id,
                    watchlist = itemExistsLocally
                )
                tmdbClient.addOrRemoveFromWatchlist(accountId, watchlistRequest)
            }
        }
        return libraryWork.isSuccess
    }

    /**
     * This function syncs favorites from server by inserting items into database and removes
     * items which are stale (i.e. not present on server) and for which no work is scheduled.
     */
    override suspend fun syncFavorites(): Boolean {
        val accountId = accountDetailsDao.getAccountDetails()?.id ?: return false

        val favoriteItemTypeString = LibraryType.FAVORITE.name.lowercase()
        return syncFromLocalAndNetwork(
            fetchFromNetwork = { mediaTypeString ->
                val favoriteItemsNetworkResults = mutableListOf<NetworkContent>()

                var favoriteItemsPage = 1
                do {
                    val result = tmdbClient.getLibraryItems(
                        accountId = accountId,
                        itemType = favoriteItemTypeString,
                        mediaType = mediaTypeString,
                        page = favoriteItemsPage++
                    ).getOrThrow().results

                    favoriteItemsNetworkResults.addAll(result)
                } while (result.isNotEmpty())

                favoriteItemsNetworkResults
            },
            fetchStaleItemsFromLocalSource = { mediaType, networkResultsPair ->
                val favoriteItems = when (mediaType) {
                    MediaType.MOVIE -> favoriteContentDao.getFavoriteMovies().first()
                    MediaType.TV -> favoriteContentDao.getFavoriteTvShows().first()
                    else -> emptyList() // Unreachable
                }

                favoriteItems
                    .filter {
                        Pair(it.mediaId, it.mediaType) !in networkResultsPair
                                && workScheduler.isWorkNotScheduled(
                            mediaId = it.mediaId,
                            mediaType = mediaType,
                            workType = LibraryType.FAVORITE
                        )
                    }
                    .map { Pair(it.mediaId, it.mediaType) }
            },
            fetchFromLocalSource = { mediaType, mediaTypeString, networkResults ->
                networkResults
                    .filter {
                        workScheduler.isWorkNotScheduled(
                            mediaId = it.id,
                            mediaType = mediaType,
                            workType = LibraryType.FAVORITE
                        )
                    }.map {
                        val contentItem = it.asModel()
                        val item = favoriteContentDao.getFavoriteItem(
                            mediaId = contentItem.id,
                            mediaType = mediaTypeString
                        )?.asLibraryItem()

                        item?.copy(
                            imagePath = contentItem.imagePath,
                            name = contentItem.name
                        ) ?: LibraryItem(
                            id = contentItem.id,
                            mediaType = mediaTypeString,
                            imagePath = contentItem.imagePath,
                            name = contentItem.name
                        )
                    }
            },
            updateLocalSource = { libraryItems, staleItems ->
                favoriteContentDao.syncFavoriteItems(
                    upsertItems = libraryItems.map { item ->
                        item.asFavoriteContentEntity()
                    },
                    deleteItems = staleItems
                )
            }
        )
    }

    /**
     * This function syncs watchlist from server by inserting items into database and removes
     * items which are stale (i.e. not present on server) and for which no work is scheduled.
     */
    override suspend fun syncWatchlist(): Boolean {
        val accountId = accountDetailsDao.getAccountDetails()?.id ?: return false

        val watchlistItemTypeString = LibraryType.WATCHLIST.name.lowercase()
        return syncFromLocalAndNetwork(
            fetchFromNetwork = { mediaTypeString ->
                val watchlistItemsNetworkResults = mutableListOf<NetworkContent>()

                var watchlistItemsPage = 1
                do {
                    val result = tmdbClient.getLibraryItems(
                        accountId = accountId,
                        itemType = watchlistItemTypeString,
                        mediaType = mediaTypeString,
                        page = watchlistItemsPage++
                    ).getOrThrow().results

                    watchlistItemsNetworkResults.addAll(result)
                } while (result.isNotEmpty())

                watchlistItemsNetworkResults
            },
            fetchStaleItemsFromLocalSource = { mediaType, networkResultsPair ->
                val watchlistItems = when (mediaType) {
                    MediaType.MOVIE -> watchlistContentDao.getMoviesWatchlist().first()
                    MediaType.TV -> watchlistContentDao.getTvShowsWatchlist().first()
                    else -> emptyList() // Unreachable
                }

                watchlistItems
                    .filter {
                        Pair(it.mediaId, it.mediaType) !in networkResultsPair
                                && workScheduler.isWorkNotScheduled(
                            mediaId = it.mediaId,
                            mediaType = mediaType,
                            workType = LibraryType.WATCHLIST
                        )
                    }
                    .map { Pair(it.mediaId, it.mediaType) }
            },
            fetchFromLocalSource = { mediaType, mediaTypeString, networkResults ->
                networkResults
                    .filter {
                        workScheduler.isWorkNotScheduled(
                            mediaId = it.id,
                            mediaType = mediaType,
                            workType = LibraryType.WATCHLIST
                        )
                    }.map {
                        val contentItem = it.asModel()
                        val item = watchlistContentDao.getWatchlistItem(
                            mediaId = contentItem.id,
                            mediaType = mediaTypeString
                        )?.asLibraryItem()

                        item?.copy(
                            imagePath = contentItem.imagePath,
                            name = contentItem.name
                        ) ?: LibraryItem(
                            id = contentItem.id,
                            mediaType = mediaTypeString,
                            imagePath = contentItem.imagePath,
                            name = contentItem.name
                        )
                    }
            },
            updateLocalSource = { libraryItems, staleItems ->
                watchlistContentDao.syncWatchlistItems(
                    upsertItems = libraryItems.map { item ->
                        item.asWatchlistContentEntity()
                    },
                    deleteItems = staleItems
                )
            }
        )
    }
}