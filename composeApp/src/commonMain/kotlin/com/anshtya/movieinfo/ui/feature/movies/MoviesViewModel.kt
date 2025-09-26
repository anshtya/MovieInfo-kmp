package com.anshtya.movieinfo.ui.feature.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anshtya.movieinfo.common.data.model.Content
import com.anshtya.movieinfo.common.data.model.category.MovieListCategory
import com.anshtya.movieinfo.common.data.repository.ContentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class MoviesViewModel(
    private val contentRepository: ContentRepository
) : ViewModel() {
    private val _nowPlayingMovies = MutableStateFlow(ContentUiState(MovieListCategory.NOW_PLAYING))
    val nowPlayingMovies = _nowPlayingMovies.asStateFlow()

    private val _popularMovies = MutableStateFlow(ContentUiState(MovieListCategory.POPULAR))
    val popularMovies = _popularMovies.asStateFlow()

    private val _topRatedMovies = MutableStateFlow(ContentUiState(MovieListCategory.TOP_RATED))
    val topRatedMovies = _topRatedMovies.asStateFlow()

    private val _upcomingMovies = MutableStateFlow(ContentUiState(MovieListCategory.UPCOMING))
    val upcomingMovies = _upcomingMovies.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    init {
        getNowPlayingMovies()
        getPopularMovies()
        getTopRatedMovies()
        getUpcomingMovies()
    }

    fun appendItems(category: MovieListCategory) {
        when (category) {
            MovieListCategory.NOW_PLAYING -> getNowPlayingMovies()
            MovieListCategory.POPULAR -> getPopularMovies()
            MovieListCategory.TOP_RATED -> getTopRatedMovies()
            MovieListCategory.UPCOMING -> getUpcomingMovies()
        }
    }

    fun onErrorShown() {
        _errorMessage.update { null }
    }

    private fun getNowPlayingMovies() {
        if (_nowPlayingMovies.value.isLoading) return

        viewModelScope.launch {
            _nowPlayingMovies.update { it.copy(isLoading = true) }

            val stateValue = _nowPlayingMovies.value
            val newPage = stateValue.page + 1

            val response = handleResult(
                contentRepository.getMovieItems(
                    page = newPage,
                    category = stateValue.category
                )
            )
            val items = response.first
            val endReached = response.second

            _nowPlayingMovies.update {
                it.copy(
                    items = (stateValue.items + items).distinct(),
                    page = newPage,
                    endReached = endReached,
                    isLoading = false
                )
            }
        }
    }

    private fun getPopularMovies() {
        if (_popularMovies.value.isLoading) return

        viewModelScope.launch {
            _popularMovies.update { it.copy(isLoading = true) }

            val stateValue = _popularMovies.value
            val newPage = stateValue.page + 1

            val response = handleResult(
                contentRepository.getMovieItems(
                    page = newPage,
                    category = stateValue.category
                )
            )
            val items = response.first
            val endReached = response.second

            _popularMovies.update {
                it.copy(
                    items = (stateValue.items + items).distinct(),
                    page = newPage,
                    endReached = endReached,
                    isLoading = false
                )
            }
        }
    }

    private fun getTopRatedMovies() {
        if (_topRatedMovies.value.isLoading) return

        viewModelScope.launch {
            _topRatedMovies.update { it.copy(isLoading = true) }

            val stateValue = _topRatedMovies.value
            val newPage = stateValue.page + 1

            val response = handleResult(
                contentRepository.getMovieItems(
                    page = newPage,
                    category = stateValue.category
                )
            )
            val items = response.first
            val endReached = response.second

            _topRatedMovies.update {
                it.copy(
                    items = (stateValue.items + items).distinct(),
                    page = newPage,
                    endReached = endReached,
                    isLoading = false
                )
            }
        }
    }

    private fun getUpcomingMovies() {
        if (_upcomingMovies.value.isLoading) return

        viewModelScope.launch {
            _upcomingMovies.update { it.copy(isLoading = true) }

            val stateValue = _upcomingMovies.value
            val newPage = stateValue.page + 1

            val response = handleResult(
                contentRepository.getMovieItems(
                    page = newPage,
                    category = stateValue.category
                )
            )
            val items = response.first
            val endReached = response.second

            _upcomingMovies.update {
                it.copy(
                    items = (stateValue.items + items).distinct(),
                    page = newPage,
                    endReached = endReached,
                    isLoading = false
                )
            }
        }
    }

    private fun handleResult(
        result: Result<List<Content>>
    ): Pair<List<Content>, Boolean> {
        return result.fold(
            onSuccess = {
                Pair(it, it.isEmpty())
            },
            onFailure = { throwable ->
                _errorMessage.update { throwable.message }
                Pair(emptyList(), false)
            }
        )
    }
}

data class ContentUiState(
    val items: List<Content>,
    val isLoading: Boolean,
    val endReached: Boolean,
    val page: Int,
    val category: MovieListCategory
) {
    constructor(category: MovieListCategory) : this(
        items = emptyList(),
        isLoading = false,
        endReached = false,
        page = 0,
        category = category
    )
}