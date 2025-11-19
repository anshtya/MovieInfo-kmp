package com.anshtya.movieinfo.ui.feature.details.content

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import movieinfo.composeapp.generated.resources.Res
import movieinfo.composeapp.generated.resources.not_available
import movieinfo.composeapp.generated.resources.overview
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun OverviewSection(
    overview: String
) {
    Column {
        Text(
            text = stringResource(Res.string.overview),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.height(2.dp))
        if (overview.isEmpty()) {
            Text(text = stringResource(Res.string.not_available))
        } else {
            Text(overview)
        }
    }
}