package com.anshtya.movieinfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anshtya.movieinfo.AppUiState.Loading
import com.anshtya.movieinfo.AppUiState.Success
import com.anshtya.movieinfo.common.data.local.datastore.preferences.SelectedDarkMode
import com.anshtya.movieinfo.common.data.repository.AuthRepository
import com.anshtya.movieinfo.common.data.repository.UserRepository
import com.anshtya.movieinfo.common.data.workscheduler.WorkScheduler
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class AppViewModel(
    userRepository: UserRepository,
    private val authRepository: AuthRepository,
    private val workScheduler: WorkScheduler
) : ViewModel() {
    val uiState: StateFlow<AppUiState> = userRepository.userData.map {
        Success(
            useDynamicColor = it.useDynamicColor,
            darkMode = it.darkMode,
            hideOnboarding = it.hideOnboarding
        )
    }.stateIn(
        scope = viewModelScope,
        initialValue = Loading,
        started = SharingStarted.WhileSubscribed(5000L),
    )

    init {
        executeLibrarySyncWork()
    }

    private fun executeLibrarySyncWork() {
        viewModelScope.launch {
            val loggedIn = authRepository.isLoggedIn.first()
            if (loggedIn) {
                workScheduler.scheduleLibrarySyncWork()
            }
        }
    }
}

sealed interface AppUiState {
    data object Loading : AppUiState
    data class Success(
        val useDynamicColor: Boolean,
        val darkMode: SelectedDarkMode,
        val hideOnboarding: Boolean
    ) : AppUiState
}