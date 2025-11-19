package com.anshtya.movieinfo.ui.feature.you.library_items

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.anshtya.movieinfo.common.data.model.LibraryType
import com.anshtya.movieinfo.common.data.model.MediaType
import kotlinx.serialization.Serializable

@Serializable
data class LibraryItems(
    val type: LibraryType
)

fun NavGraphBuilder.libraryItemsScreen(
    navigateToDetails: (Int, MediaType) -> Unit,
    onNavigateUp: () -> Unit
) {
    composable<LibraryItems> {
        LibraryItemsRoute(
            onBackClick = onNavigateUp,
            navigateToDetails = navigateToDetails
        )
    }
}