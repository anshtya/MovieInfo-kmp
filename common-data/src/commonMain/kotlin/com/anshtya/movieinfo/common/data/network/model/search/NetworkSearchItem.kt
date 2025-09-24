package com.anshtya.movieinfo.common.data.network.model.search

import com.anshtya.movieinfo.common.data.model.SearchItem
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkSearchItem(
    val id: Int,
    val name: String?,
    val title: String?,
    @SerialName("media_type") val mediaType: String,
    @SerialName("poster_path") val posterPath: String?,
    @SerialName("profile_path") val profilePath: String?,
)

fun NetworkSearchItem.asModel(): SearchItem {
    return SearchItem(
        id = id,
        name = title ?: name ?: "",
        mediaType = mediaType,
        imagePath = posterPath ?: profilePath ?: ""
    )
}