package com.anshtya.movieinfo.ui.feature.items

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.anshtya.movieinfo.common.data.model.Content
import com.anshtya.movieinfo.common.data.model.category.MovieListCategory
import com.anshtya.movieinfo.common.data.model.category.TvSeriesListCategory
import com.anshtya.movieinfo.common.data.repository.ContentRepository
import com.anshtya.movieinfo.ui.FeedType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class ItemsViewModel(
    savedStateHandle: SavedStateHandle,
    private val contentRepository: ContentRepository
) : ViewModel() {
    val item = savedStateHandle.toRoute<Items>()

    private val _uiState = MutableStateFlow(ItemsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        appendItems()
    }

    fun appendItems() {
        if (_uiState.value.isLoading) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val stateValue = _uiState.value
            val newPage = stateValue.page + 1

            val items = when (item.type) {
                FeedType.MOVIE -> {
                    contentRepository.getMovieItems(
                        page = newPage,
                        category = MovieListCategory.valueOf(item.category)
                    )
                }

                FeedType.TV -> {
                    contentRepository.getTvSeriesItems(
                        page = newPage,
                        category = TvSeriesListCategory.valueOf(item.category)
                    )
                }
            }.getOrElse { emptyList() }

            _uiState.update {
                it.copy(
                    items = (stateValue.items + items).distinct(),
                    page = newPage,
                    endReached = items.isEmpty(),
                    isLoading = false
                )
            }
        }
    }
}

data class ItemsUiState(
    val items: List<Content> = emptyList(),
    val isLoading: Boolean = false,
    val endReached: Boolean = false,
    val page: Int = 0,
)