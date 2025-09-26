package com.anshtya.movieinfo.feature.you.library_items

import androidx.lifecycle.SavedStateHandle
import com.anshtya.movieinfo.MainDispatcherRule
import com.anshtya.movieinfo.common.data.model.LibraryType
import com.anshtya.movieinfo.common.data.repository.test.TestLibraryRepository
import com.anshtya.movieinfo.common.data.repository.test.data.testLibraryItems
import com.anshtya.movieinfo.ui.feature.you.libraryTypeNavigationArgument
import com.anshtya.movieinfo.ui.feature.you.library_items.LibraryItemsViewModel
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LibraryItemsViewModelTest {
    private val libraryRepository = TestLibraryRepository()
    private lateinit var viewModel: LibraryItemsViewModel

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `test favorite items initialization`() = runTest {
        viewModel = createViewModel(navigationArgument = LibraryType.FAVORITE.name)

        val libraryTypeCollectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.libraryType.collect()
        }
        val moviesCollectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.movieItems.collect()
        }
        val tvShowsCollectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.tvItems.collect()
        }

        assertEquals(
            LibraryType.FAVORITE,
            viewModel.libraryType.value
        )

        assertEquals(
            1,
            viewModel.movieItems.value.size
        )

        assertEquals(
            1,
            viewModel.tvItems.value.size
        )

        libraryTypeCollectJob.cancel()
        moviesCollectJob.cancel()
        tvShowsCollectJob.cancel()
    }

    @Test
    fun `test watchlist items initialization`() = runTest {
        viewModel = createViewModel(navigationArgument = LibraryType.WATCHLIST.name)

        val libraryTypeCollectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.libraryType.collect()
        }
        val moviesCollectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.movieItems.collect()
        }
        val tvShowsCollectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.tvItems.collect()
        }

        assertEquals(
            LibraryType.WATCHLIST,
            viewModel.libraryType.value
        )

        assertEquals(
            1,
            viewModel.movieItems.value.size
        )

        assertEquals(
            1,
            viewModel.tvItems.value.size
        )

        libraryTypeCollectJob.cancel()
        moviesCollectJob.cancel()
        tvShowsCollectJob.cancel()
    }

    @Test
    fun `test delete favorite item`() = runTest{
        viewModel = createViewModel(navigationArgument = LibraryType.FAVORITE.name)

        val libraryTypeCollectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.libraryType.collect()
        }
        val moviesCollectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.movieItems.collect()
        }
        val tvShowsCollectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.tvItems.collect()
        }

        val testMovie = testLibraryItems[0]
        val testTvShow = testLibraryItems[1]

        viewModel.deleteItem(testMovie)

        assertEquals(
            0,
            viewModel.movieItems.value.size
        )

        viewModel.deleteItem(testTvShow)
        assertEquals(
            0,
            viewModel.tvItems.value.size
        )

        libraryTypeCollectJob.cancel()
        moviesCollectJob.cancel()
        tvShowsCollectJob.cancel()
    }

    @Test
    fun `test delete watchlist item`() = runTest {
        viewModel = createViewModel(navigationArgument = LibraryType.WATCHLIST.name)

        val libraryTypeCollectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.libraryType.collect()
        }
        val moviesCollectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.movieItems.collect()
        }
        val tvShowsCollectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.tvItems.collect()
        }

        val testMovie = testLibraryItems[0]
        val testTvShow = testLibraryItems[1]

        viewModel.deleteItem(testMovie)
        assertEquals(
            0,
            viewModel.movieItems.value.size
        )

        viewModel.deleteItem(testTvShow)
        assertEquals(
            0,
            viewModel.tvItems.value.size
        )

        libraryTypeCollectJob.cancel()
        moviesCollectJob.cancel()
        tvShowsCollectJob.cancel()
    }

    @Test
    fun `test error in when deleting item`() = runTest {
        viewModel = createViewModel(navigationArgument = LibraryType.FAVORITE.name)

        val libraryTypeCollectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.libraryType.collect()
        }
        val errorCollectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.errorMessage.collect()
        }

        val testItem = testLibraryItems[0]
        libraryRepository.generateError(true)

        viewModel.deleteItem(testItem)

        assertTrue(viewModel.errorMessage.value != null)

        libraryTypeCollectJob.cancel()
        errorCollectJob.cancel()
    }

    @Test
    fun `test error message reset`() {
        viewModel = createViewModel(navigationArgument = LibraryType.FAVORITE.name)

        viewModel.onErrorShown()

        assertNull(viewModel.errorMessage.value)
    }

    private fun createViewModel(
        navigationArgument: String
    ) = LibraryItemsViewModel(
        savedStateHandle = SavedStateHandle(
            mapOf(libraryTypeNavigationArgument to navigationArgument)
        ),
        libraryRepository = libraryRepository
    )
}