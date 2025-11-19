package com.anshtya.movieinfo.ui.feature.items

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.anshtya.movieinfo.common.data.model.FeedType
import com.anshtya.movieinfo.common.data.model.MediaType
import kotlinx.serialization.Serializable

@Serializable
data class Items(
    val type: FeedType,
    val category: String
)

fun NavGraphBuilder.itemsScreen(
    onNavigateUp: () -> Unit,
    onNavigateToItem: (Int, MediaType) -> Unit
) {
    composable<Items> {
        ItemsRoute(
            onNavigateUp = onNavigateUp,
            onNavigateToItem = onNavigateToItem
        )
    }
}