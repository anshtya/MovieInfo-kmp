package com.anshtya.movieinfo.ui.feature.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.anshtya.movieinfo.common.data.model.LibraryItem
import com.anshtya.movieinfo.common.data.model.MediaType
import com.anshtya.movieinfo.common.data.model.details.MovieDetails
import com.anshtya.movieinfo.common.data.model.details.tv.TvSeriesDetails
import com.anshtya.movieinfo.common.data.model.people.PersonDetails
import com.anshtya.movieinfo.common.data.repository.AuthRepository
import com.anshtya.movieinfo.common.data.repository.DetailsRepository
import com.anshtya.movieinfo.common.data.repository.LibraryRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@OptIn(ExperimentalCoroutinesApi::class)
@KoinViewModel
class DetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val detailsRepository: DetailsRepository,
    private val libraryRepository: LibraryRepository,
    private val authRepository: AuthRepository
) : ViewModel() {
    private val details = savedStateHandle.toRoute<Details>()
    private val detailsFlow = MutableStateFlow<Details?>(null)

    private val _uiState = MutableStateFlow(DetailsUiState())
    val uiState = _uiState.asStateFlow()

    val contentDetailsUiState: StateFlow<ContentDetailUiState> = detailsFlow
        .filterNotNull()
        .mapLatest {
            when (it.type) {
                MediaType.MOVIE -> {
                    val response = detailsRepository.getMovieDetails(it.id)
                    handleMovieDetailsResult(response)
                }

                MediaType.TV -> {
                    val response = detailsRepository.getTvSeriesDetails(it.id)
                    handleTvSeriesDetailsResult(response)
                }

                MediaType.PERSON -> {
                    val response = detailsRepository.getPersonDetails(it.id)
                    handlePeopleDetailsResult(response)
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = ContentDetailUiState.Loading
        )

    init {
        detailsFlow.update { details }
    }

    fun addOrRemoveFavorite(libraryItem: LibraryItem) {
        viewModelScope.launch {
            val isLoggedIn = authRepository.isLoggedIn.first()
            if (isLoggedIn) {
                _uiState.update { it.copy(markedFavorite = !(it.markedFavorite)) }
                libraryRepository.addOrRemoveFavorite(libraryItem).onFailure {
                    _uiState.update {
                        it.copy(
                            markedFavorite = !(it.markedFavorite),
                            errorMessage = "An error occurred"
                        )
                    }
                }
            } else {
                _uiState.update { it.copy(showSignInSheet = true) }
            }
        }
    }

    fun addOrRemoveFromWatchlist(libraryItem: LibraryItem) {
        viewModelScope.launch {
            val isLoggedIn = authRepository.isLoggedIn.first()
            if (isLoggedIn) {
                _uiState.update { it.copy(savedInWatchlist = !(it.savedInWatchlist)) }
                libraryRepository.addOrRemoveFromWatchlist(libraryItem).onFailure {
                    _uiState.update {
                        it.copy(
                            savedInWatchlist = !(it.savedInWatchlist),
                            errorMessage = "An error occurred"
                        )
                    }
                }
            } else {
                _uiState.update { it.copy(showSignInSheet = true) }
            }
        }
    }

    fun onErrorShown() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    fun onHideBottomSheet() {
        _uiState.update { it.copy(showSignInSheet = false) }
    }

    private suspend fun handleMovieDetailsResult(
        result: Result<MovieDetails>
    ): ContentDetailUiState {
        return result.fold(
            onSuccess = { data ->
                _uiState.update {
                    it.copy(
                        markedFavorite = libraryRepository.itemInFavoritesExists(
                            mediaId = data.id,
                            mediaType = MediaType.MOVIE
                        ),
                        savedInWatchlist = libraryRepository.itemInWatchlistExists(
                            mediaId = data.id,
                            mediaType = MediaType.MOVIE
                        )
                    )
                }
                ContentDetailUiState.Movie(data = data)
            },
            onFailure = { throwable ->
                _uiState.update { it.copy(errorMessage = throwable.message) }
                ContentDetailUiState.Empty
            }
        )
    }

    private suspend fun handleTvSeriesDetailsResult(
        result: Result<TvSeriesDetails>
    ): ContentDetailUiState {
        return result.fold(
            onSuccess = { data ->
                _uiState.update {
                    it.copy(
                        markedFavorite = libraryRepository.itemInFavoritesExists(
                            mediaId = data.id,
                            mediaType = MediaType.TV
                        ),
                        savedInWatchlist = libraryRepository.itemInWatchlistExists(
                            mediaId = data.id,
                            mediaType = MediaType.TV
                        )
                    )
                }
                ContentDetailUiState.TvSeries(data = data)
            },
            onFailure = { throwable ->
                _uiState.update { it.copy(errorMessage = throwable.message) }
                ContentDetailUiState.Empty
            }
        )
    }

    private fun handlePeopleDetailsResult(
        result: Result<PersonDetails>
    ): ContentDetailUiState {
        return result.fold(
            onSuccess = { data ->
                ContentDetailUiState.Person(data = data)
            },
            onFailure = { throwable ->
                _uiState.update { it.copy(errorMessage = throwable.message) }
                ContentDetailUiState.Empty
            }
        )
    }
}

data class DetailsUiState(
    val markedFavorite: Boolean = false,
    val savedInWatchlist: Boolean = false,
    val showSignInSheet: Boolean = false,
    val errorMessage: String? = null
)

sealed interface ContentDetailUiState {
    data object Loading : ContentDetailUiState
    data object Empty : ContentDetailUiState
    data class Movie(val data: MovieDetails) : ContentDetailUiState
    data class TvSeries(val data: TvSeriesDetails) : ContentDetailUiState
    data class Person(val data: PersonDetails) : ContentDetailUiState
}