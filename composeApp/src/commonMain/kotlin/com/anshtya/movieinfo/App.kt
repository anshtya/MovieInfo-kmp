package com.anshtya.movieinfo

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.anshtya.movieinfo.common.data.local.datastore.preferences.SelectedDarkMode
import com.anshtya.movieinfo.navigation.MovieInfoNavigation
import com.anshtya.movieinfo.ui.theme.MovieInfoTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App(
    onHideSplashScreen: () -> Unit = {},
    viewModel: AppViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val initialized by remember {
        derivedStateOf { uiState != AppUiState.Loading }
    }
    LaunchedEffect(initialized) {
        if (initialized) onHideSplashScreen()
    }

    MovieInfoTheme(
        darkTheme = shouldUseDarkTheme(uiState),
        dynamicColor = shouldUseDynamicColor(uiState)
    ) {
        if (uiState is AppUiState.Success) {
            MovieInfoNavigation(
                hideOnboarding = (uiState as AppUiState.Success).hideOnboarding
            )
        }
    }
}

@Composable
private fun shouldUseDarkTheme(
    uiState: AppUiState
): Boolean {
    return when (uiState) {
        AppUiState.Loading -> isSystemInDarkTheme()
        is AppUiState.Success -> when (uiState.darkMode) {
            SelectedDarkMode.SYSTEM -> isSystemInDarkTheme()
            SelectedDarkMode.DARK -> true
            SelectedDarkMode.LIGHT -> false
        }
    }
}

@Composable
private fun shouldUseDynamicColor(
    uiState: AppUiState
): Boolean {
    return when (uiState) {
        AppUiState.Loading -> false
        is AppUiState.Success -> uiState.useDynamicColor
    }
}