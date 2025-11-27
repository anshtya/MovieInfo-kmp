package com.anshtya.movieinfo.ui.theme

import android.os.Build
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import com.anshtya.movieinfo.ui.feature.you.SupportsDynamicTheme

@Composable
actual fun MovieInfoTheme(
    darkTheme: Boolean,
    dynamicColor: Boolean,
    content: @Composable (() -> Unit)
) {
    val supportsDynamicTheme = supportsDynamicColorTheme()
    val colorScheme = when {
        dynamicColor && supportsDynamicTheme -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> darkScheme
        else -> lightScheme
    }

    CompositionLocalProvider(SupportsDynamicTheme provides supportsDynamicTheme) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography
        ) {
            Surface(
                modifier = Modifier
                    .semantics { testTagsAsResourceId = true }
                    .fillMaxSize(),
                content = content
            )
        }
    }
}

fun supportsDynamicColorTheme() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S