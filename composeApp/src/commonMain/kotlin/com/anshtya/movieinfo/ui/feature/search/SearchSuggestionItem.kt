package com.anshtya.movieinfo.ui.feature.search

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.anshtya.movieinfo.ui.component.MediaItemCard
import com.anshtya.movieinfo.ui.component.noRippleClickable

@Composable
internal fun SearchSuggestionItem(
    name: String,
    imagePath: String,
    onItemClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.noRippleClickable { onItemClick() }
    ) {
        MediaItemCard(
            posterPath = imagePath,
            onItemClick = onItemClick
        )
        Text(
            text = name,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )
    }
}