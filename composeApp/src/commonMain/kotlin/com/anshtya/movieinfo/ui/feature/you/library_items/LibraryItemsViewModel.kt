package com.anshtya.movieinfo.ui.feature.you.library_items

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anshtya.movieinfo.common.data.model.LibraryItem
import com.anshtya.movieinfo.common.data.model.LibraryType
import com.anshtya.movieinfo.common.data.repository.LibraryRepository
import com.anshtya.movieinfo.ui.feature.you.libraryTypeNavigationArgument
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
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

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    private val libraryTypeString = savedStateHandle.getStateFlow(
        key = libraryTypeNavigationArgument,
        initialValue = ""
    )

    val libraryType: StateFlow<LibraryType?> = libraryTypeString
        .map {
            if (it.isEmpty()) null
            else enumValueOf<LibraryType>(it)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = null
        )

    val movieItems: StateFlow<List<LibraryItem>> = libraryType
        .flatMapLatest { itemType ->
            itemType?.let {
                when (it) {
                    LibraryType.FAVORITE -> libraryRepository.favoriteMovies
                    LibraryType.WATCHLIST -> libraryRepository.moviesWatchlist
                }
            } ?: flow {}
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )

    val tvItems: StateFlow<List<LibraryItem>> = libraryType
        .flatMapLatest { itemType ->
            itemType?.let {
                when (it) {
                    LibraryType.FAVORITE -> libraryRepository.favoriteTvShows
                    LibraryType.WATCHLIST -> libraryRepository.tvShowsWatchlist
                }
            } ?: flow {}
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )

    fun deleteItem(libraryItem: LibraryItem) {
        viewModelScope.launch {
            when (libraryType.value) {
                LibraryType.FAVORITE -> {
                    libraryRepository.addOrRemoveFavorite(libraryItem)
                }

                LibraryType.WATCHLIST -> {
                    libraryRepository.addOrRemoveFromWatchlist(libraryItem)
                }

                else -> Result.success(Unit)
            }.onFailure {
                _errorMessage.update { "An error occurred" }
            }
        }
    }

    fun onErrorShown() {
        _errorMessage.update { null }
    }
}