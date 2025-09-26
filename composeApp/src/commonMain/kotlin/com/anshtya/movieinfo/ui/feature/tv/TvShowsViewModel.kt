package com.anshtya.movieinfo.ui.feature.tv

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anshtya.movieinfo.common.data.model.Content
import com.anshtya.movieinfo.common.data.model.category.TvSeriesListCategory
import com.anshtya.movieinfo.common.data.repository.ContentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class TvShowsViewModel(
    private val contentRepository: ContentRepository
) : ViewModel() {
    private val _airingTodayTvShows = MutableStateFlow(
        ContentUiState(TvSeriesListCategory.AIRING_TODAY)
    )
    val airingTodayTvShows = _airingTodayTvShows.asStateFlow()

    private val _onAirTvShows = MutableStateFlow(ContentUiState(TvSeriesListCategory.ON_THE_AIR))
    val onAirTvShows = _onAirTvShows.asStateFlow()

    private val _popularTvShows = MutableStateFlow(ContentUiState(TvSeriesListCategory.POPULAR))
    val popularTvShows = _popularTvShows.asStateFlow()

    private val _topRatedTvShows = MutableStateFlow(ContentUiState(TvSeriesListCategory.TOP_RATED))
    val topRatedTvShows = _topRatedTvShows.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    init {
        getAiringTodayTvShows()
        getOnAirTvShows()
        getPopularTvShows()
        getTopRatedTvShows()
    }

    fun appendItems(category: TvSeriesListCategory) {
        when (category) {
            TvSeriesListCategory.AIRING_TODAY -> getAiringTodayTvShows()
            TvSeriesListCategory.ON_THE_AIR -> getOnAirTvShows()
            TvSeriesListCategory.POPULAR -> getPopularTvShows()
            TvSeriesListCategory.TOP_RATED -> getTopRatedTvShows()
        }
    }

    fun onErrorShown() {
        _errorMessage.update { null }
    }

    private fun getAiringTodayTvShows() {
        if (_airingTodayTvShows.value.isLoading) return

        viewModelScope.launch {
            _airingTodayTvShows.update { it.copy(isLoading = true) }

            val stateValue = _airingTodayTvShows.value
            val newPage = stateValue.page + 1

            val result = handleResult(
                contentRepository.getTvSeriesItems(
                    page = newPage,
                    category = stateValue.category
                )
            )
            val items = result.first
            val endReached = result.second

            _airingTodayTvShows.update {
                it.copy(
                    items = (stateValue.items + items).distinct(),
                    page = newPage,
                    endReached = endReached,
                    isLoading = false
                )
            }
        }
    }

    private fun getOnAirTvShows() {
        if (_onAirTvShows.value.isLoading) return

        viewModelScope.launch {
            _onAirTvShows.update { it.copy(isLoading = true) }

            val stateValue = _onAirTvShows.value
            val newPage = stateValue.page + 1

            val response = handleResult(
                contentRepository.getTvSeriesItems(
                    page = newPage,
                    category = stateValue.category
                )
            )
            val items = response.first
            val endReached = response.second

            _onAirTvShows.update {
                it.copy(
                    items = (stateValue.items + items).distinct(),
                    page = newPage,
                    endReached = endReached,
                    isLoading = false
                )
            }
        }
    }

    private fun getPopularTvShows() {
        if (_popularTvShows.value.isLoading) return

        viewModelScope.launch {
            _popularTvShows.update { it.copy(isLoading = true) }

            val stateValue = _popularTvShows.value
            val newPage = stateValue.page + 1

            val response = handleResult(
                contentRepository.getTvSeriesItems(
                    page = newPage,
                    category = stateValue.category
                )
            )
            val items = response.first
            val endReached = response.second

            _popularTvShows.update {
                it.copy(
                    items = (stateValue.items + items).distinct(),
                    page = newPage,
                    endReached = endReached,
                    isLoading = false
                )
            }
        }
    }

    private fun getTopRatedTvShows() {
        if (_topRatedTvShows.value.isLoading) return

        viewModelScope.launch {
            _topRatedTvShows.update { it.copy(isLoading = true) }

            val stateValue = _topRatedTvShows.value
            val newPage = stateValue.page + 1

            val response = handleResult(
                contentRepository.getTvSeriesItems(
                    page = newPage,
                    category = stateValue.category
                )
            )
            val items = response.first
            val endReached = response.second

            _topRatedTvShows.update {
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
    val category: TvSeriesListCategory
) {
    constructor(category: TvSeriesListCategory) : this(
        items = emptyList(),
        isLoading = false,
        endReached = false,
        page = 0,
        category = category
    )
}