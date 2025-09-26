package com.anshtya.movieinfo.feature.you

import com.anshtya.movieinfo.MainDispatcherRule
import com.anshtya.movieinfo.common.data.local.datastore.preferences.SelectedDarkMode
import com.anshtya.movieinfo.common.data.repository.test.TestAuthRepository
import com.anshtya.movieinfo.common.data.repository.test.TestUserRepository
import com.anshtya.movieinfo.common.data.repository.test.testAccountDetails
import com.anshtya.movieinfo.common.data.repository.test.testUserData
import com.anshtya.movieinfo.ui.feature.you.UserSettings
import com.anshtya.movieinfo.ui.feature.you.YouUiState
import com.anshtya.movieinfo.ui.feature.you.YouViewModel
import junit.framework.TestCase
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class YouViewModelTest {
    private val authRepository = TestAuthRepository()
    private val userRepository = TestUserRepository()
    private lateinit var viewModel: YouViewModel

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        viewModel = YouViewModel(
            authRepository = authRepository,
            userRepository = userRepository
        )
    }

    @Test
    fun `test initial state`() {
        TestCase.assertEquals(
            YouUiState(),
            viewModel.uiState.value
        )
    }

    @Test
    fun `test account details`() = runTest {
        val uiStateCollectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.uiState.collect()
        }
        val loggedInCollectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.isLoggedIn.collect()
        }

        authRepository.setAuthStatus(isLoggedIn = true)
        assertEquals(
            YouUiState(accountDetails = testAccountDetails),
            viewModel.uiState.value
        )

        uiStateCollectJob.cancel()
        loggedInCollectJob.cancel()
    }

    @Test
    fun `test user settings`() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.userSettings.collect()
        }

        assertEquals(
            UserSettings(
                useDynamicColor = testUserData.useDynamicColor,
                includeAdultResults = testUserData.includeAdultResults,
                darkMode = testUserData.darkMode
            ),
            viewModel.userSettings.value
        )

        collectJob.cancel()
    }

    @Test
    fun `test error message reset`() {
        viewModel.onErrorShown()

        assertNull(viewModel.uiState.value.errorMessage)
    }

    @Test
    fun `test preference updates`() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.userSettings.collect()
        }

        viewModel.setDynamicColorPreference(true)
        assertEquals(
            true,
            viewModel.userSettings.value?.useDynamicColor
        )

        viewModel.setDarkModePreference(SelectedDarkMode.LIGHT)
        assertEquals(
            SelectedDarkMode.LIGHT,
            viewModel.userSettings.value?.darkMode
        )

        viewModel.setAdultResultPreference(true)
        assertEquals(
            true,
            viewModel.userSettings.value?.includeAdultResults
        )

        collectJob.cancel()
    }

    @Test
    fun `test logout error`() = runTest {
        val uiStateCollectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.uiState.collect()
        }
        val loggedInCollectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.isLoggedIn.collect()
        }

        with(authRepository) {
            setAuthStatus(true)
            generateError(true)
        }

        viewModel.logOut()

        assertTrue(viewModel.uiState.value.errorMessage != null)

        uiStateCollectJob.cancel()
        loggedInCollectJob.cancel()
    }

    @Test
    fun `test refresh error`() = runTest {
        val uiStateCollectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.uiState.collect()
        }
        val loggedInCollectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.isLoggedIn.collect()
        }

        authRepository.setAuthStatus(true)
        userRepository.generateError(true)
        viewModel.onRefresh()

        assertTrue(viewModel.uiState.value.errorMessage != null)

        uiStateCollectJob.cancel()
        loggedInCollectJob.cancel()
    }
}