package com.anshtya.movieinfo.ui.feature.you.library_items

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.anshtya.movieinfo.common.data.model.LibraryItem
import com.anshtya.movieinfo.common.data.model.LibraryType
import com.anshtya.movieinfo.common.data.repository.LibraryRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@OptIn(ExperimentalCoroutinesApi::class)
@KoinViewModel
class LibraryItemsViewModel(
    savedStateHandle: SavedStateHandle,
    private val libraryRepository: LibraryRepository
) : ViewModel() {
    val libraryType = savedStateHandle.toRoute<LibraryItems>().type
    private val libraryTypeFlow = MutableStateFlow<LibraryType?>(null)

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    val movieItems: StateFlow<List<LibraryItem>> = libraryTypeFlow
        .filterNotNull()
        .flatMapLatest {
            when (it) {
                LibraryType.FAVORITE -> libraryRepository.favoriteMovies
                LibraryType.WATCHLIST -> libraryRepository.moviesWatchlist
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )

    val tvItems: StateFlow<List<LibraryItem>> = libraryTypeFlow
        .filterNotNull()
        .flatMapLatest {
            when (it) {
                LibraryType.FAVORITE -> libraryRepository.favoriteTvShows
                LibraryType.WATCHLIST -> libraryRepository.tvShowsWatchlist
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )

    init {
        libraryTypeFlow.update { libraryType }
    }

    fun deleteItem(libraryItem: LibraryItem) {
        viewModelScope.launch {
            when (libraryType) {
                LibraryType.FAVORITE -> {
                    libraryRepository.addOrRemoveFavorite(libraryItem)
                }

                LibraryType.WATCHLIST -> {
                    libraryRepository.addOrRemoveFromWatchlist(libraryItem)
                }
            }.onFailure {
                _errorMessage.update { "An error occurred" }
            }
        }
    }

    fun onErrorShown() {
        _errorMessage.update { null }
    }
}