package com.anshtya.movieinfo.ui.feature.you

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anshtya.movieinfo.common.data.local.datastore.preferences.SelectedDarkMode
import com.anshtya.movieinfo.common.data.model.AccountDetails
import com.anshtya.movieinfo.common.data.repository.AuthRepository
import com.anshtya.movieinfo.common.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class YouViewModel(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(YouUiState())
    val uiState = _uiState.asStateFlow()

    val isLoggedIn = authRepository.isLoggedIn
        .onEach { isLoggedIn ->
            if (isLoggedIn) getAccountDetails()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = null
        )

    val userSettings: StateFlow<UserSettings?> = userRepository.userData
        .map {
            UserSettings(
                useDynamicColor = it.useDynamicColor,
                includeAdultResults = it.includeAdultResults,
                darkMode = it.darkMode
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = null
        )

    fun setDynamicColorPreference(useDynamicColor: Boolean) {
        viewModelScope.launch {
            userRepository.setDynamicColorPreference(useDynamicColor)
        }
    }

    fun setAdultResultPreference(includeAdultResults: Boolean) {
        viewModelScope.launch {
            userRepository.setAdultResultPreference(includeAdultResults)
        }
    }

    fun setDarkModePreference(selectedDarkMode: SelectedDarkMode) {
        viewModelScope.launch {
            userRepository.setDarkModePreference(selectedDarkMode)
        }
    }

    fun getAccountDetails() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val accountDetails = userRepository.getAccountDetails()
            _uiState.update {
                it.copy(
                    isLoading = false,
                    accountDetails = accountDetails
                )
            }
        }
    }

    fun logOut() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoggingOut = true) }

            authRepository.logout(
                accountId = _uiState.value.accountDetails!!.id
            ).onFailure { throwable ->
                _uiState.update { it.copy(errorMessage = throwable.message) }
            }

            _uiState.update { it.copy(isLoggingOut = false) }
        }
    }

    fun onRefresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true) }

            userRepository.updateAccountDetails(
                accountId = _uiState.value.accountDetails!!.id
            ).onFailure { throwable ->
                _uiState.update { it.copy(errorMessage = throwable.message) }
            }

            _uiState.update { it.copy(isRefreshing = false) }
        }
    }

    fun onErrorShown() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}

data class YouUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val isLoggingOut: Boolean = false,
    val accountDetails: AccountDetails? = null,
    val errorMessage: String? = null
)

data class UserSettings(
    val useDynamicColor: Boolean,
    val includeAdultResults: Boolean,
    val darkMode: SelectedDarkMode,
)