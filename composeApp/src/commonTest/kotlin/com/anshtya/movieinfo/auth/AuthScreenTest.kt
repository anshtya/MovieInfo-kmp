package com.anshtya.movieinfo.auth

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.runComposeUiTest
import com.anshtya.movieinfo.ui.feature.auth.AuthScreen
import com.anshtya.movieinfo.ui.feature.auth.AuthUiState
import movieinfo.composeapp.generated.resources.Res
import movieinfo.composeapp.generated.resources.auth_circular_progress_indicator
import movieinfo.composeapp.generated.resources.continue_without_sign_in
import movieinfo.composeapp.generated.resources.sign_in
import org.jetbrains.compose.resources.getString
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class AuthScreenTest {
    @Test
    fun snackBar_whenError_appears() = runComposeUiTest {
        val errorMessage = "error message"
        setContent {
            AuthScreen(
                uiState = AuthUiState(
                    errorMessage = errorMessage
                ),
                hideOnboarding = null,
                onLogInClick = {},
                onErrorShown = {},
                onUsernameChange = {},
                onPasswordChange = {},
                onBackClick = {},
                onContinueWithoutSignInClick = {}
            )
        }

        onNodeWithText(errorMessage).assertIsDisplayed()
    }

    @Test
    fun circularProgressIndicator_whenLoading_appears() = runComposeUiTest {
        val authIndicatorDescription = getString(Res.string.auth_circular_progress_indicator)

        val signInText = getString(Res.string.sign_in)

        setContent {
            AuthScreen(
                uiState = AuthUiState(
                    isLoading = true
                ),
                hideOnboarding = null,
                onLogInClick = {},
                onErrorShown = {},
                onUsernameChange = {},
                onPasswordChange = {},
                onBackClick = {},
                onContinueWithoutSignInClick = {}
            )
        }

        onNode(hasContentDescription(authIndicatorDescription))
            .assertIsDisplayed()

        onNode(hasContentDescription(signInText))
            .assertIsNotDisplayed()
    }

    @Test
    fun continueWithoutSignIn_whenPreviouslyOnboarded_hidden() = runComposeUiTest {
        val continueText = getString(Res.string.continue_without_sign_in)

        setContent {
            AuthScreen(
                uiState = AuthUiState(),
                hideOnboarding = null,
                onLogInClick = {},
                onErrorShown = {},
                onUsernameChange = {},
                onPasswordChange = {},
                onBackClick = {},
                onContinueWithoutSignInClick = {}
            )
        }

        onNodeWithText(continueText).assertDoesNotExist()
    }

    @Test
    fun continueWithoutSignIn_whenNotOnboarded_shown() = runComposeUiTest {
        val continueText = getString(Res.string.continue_without_sign_in)

        setContent {
            AuthScreen(
                uiState = AuthUiState(),
                hideOnboarding = false,
                onLogInClick = {},
                onErrorShown = {},
                onUsernameChange = {},
                onPasswordChange = {},
                onBackClick = {},
                onContinueWithoutSignInClick = {}
            )
        }

        onNodeWithText(continueText).assertIsDisplayed()
    }
}