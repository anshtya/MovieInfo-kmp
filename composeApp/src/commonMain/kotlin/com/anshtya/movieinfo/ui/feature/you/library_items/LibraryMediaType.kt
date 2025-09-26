package com.anshtya.movieinfo.ui.feature.you.library_items

import movieinfo.composeapp.generated.resources.Res
import movieinfo.composeapp.generated.resources.movies
import movieinfo.composeapp.generated.resources.tv_shows
import org.jetbrains.compose.resources.StringResource

enum class LibraryMediaType(
    val displayName: StringResource
) {
    MOVIE(Res.string.movies),
    TV(Res.string.tv_shows)
}