package com.anshtya.movieinfo.ui

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.runComposeUiTest
import com.anshtya.movieinfo.ui.component.TmdbImage
import movieinfo.composeapp.generated.resources.Res
import movieinfo.composeapp.generated.resources.no_image_available
import org.jetbrains.compose.resources.getString
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class TmdbImageTest {
    @Test
    fun textDisplayed_whenImageUrlIsEmpty() = runComposeUiTest {
        val noImageAvailableDescription = getString(Res.string.no_image_available)
        setContent {
            TmdbImage(width = 500, imageUrl = "")
        }

        onNodeWithText(noImageAvailableDescription).assertIsDisplayed()
    }
}