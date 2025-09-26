package com.anshtya.movieinfo.details

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.runComposeUiTest
import com.anshtya.movieinfo.ui.feature.details.ContentDetailUiState
import com.anshtya.movieinfo.ui.feature.details.DetailsScreen
import com.anshtya.movieinfo.ui.feature.details.DetailsUiState
import movieinfo.composeapp.generated.resources.Res
import movieinfo.composeapp.generated.resources.details_loading
import org.jetbrains.compose.resources.getString
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class DetailsScreenTest {
    @Test
    fun circularProgressIndicator_whenLoading_displayed() = runComposeUiTest {
        val loadingContentDescription = getString(Res.string.details_loading)

        setContent {
            DetailsScreen(
                uiState = DetailsUiState(),
                contentDetailsUiState = ContentDetailUiState.Loading,
                onHideBottomSheet = {},
                onErrorShown = {},
                onFavoriteClick = {},
                onWatchlistClick = {},
                onItemClick = {},
                onSeeAllCastClick = {},
                onSignInClick = {},
                onBackClick = {}
            )
        }

        onNodeWithContentDescription(loadingContentDescription).assertIsDisplayed()
    }
}