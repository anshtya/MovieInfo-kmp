package com.anshtya.movieinfo.ui.feature.auth

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object Auth

fun NavGraphBuilder.authScreen(
    onBackClick: () -> Unit,
) {
    composable<Auth> {
        AuthRoute(onBackClick = onBackClick)
    }
}