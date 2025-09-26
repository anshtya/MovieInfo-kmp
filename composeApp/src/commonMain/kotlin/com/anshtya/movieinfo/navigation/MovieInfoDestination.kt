package com.anshtya.movieinfo.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LiveTv
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.LiveTv
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Search
import androidx.compose.ui.graphics.vector.ImageVector
import movieinfo.composeapp.generated.resources.Res
import movieinfo.composeapp.generated.resources.movies
import movieinfo.composeapp.generated.resources.search
import movieinfo.composeapp.generated.resources.tv_shows
import movieinfo.composeapp.generated.resources.you
import org.jetbrains.compose.resources.StringResource

enum class MovieInfoDestination(
    val title: StringResource,
    val selectedIcon: ImageVector,
    val icon: ImageVector
) {
    MOVIES(
        title = Res.string.movies,
        selectedIcon = Icons.Rounded.Home,
        icon = Icons.Outlined.Home
    ),
    TV_SHOWS(
        title = Res.string.tv_shows,
        selectedIcon = Icons.Rounded.LiveTv,
        icon = Icons.Outlined.LiveTv
    ),
    SEARCH(
        title = Res.string.search,
        selectedIcon = Icons.Rounded.Search,
        icon = Icons.Outlined.Search
    ),
    YOU(
        title = Res.string.you,
        selectedIcon = Icons.Rounded.Person,
        icon = Icons.Outlined.Person
    )
}