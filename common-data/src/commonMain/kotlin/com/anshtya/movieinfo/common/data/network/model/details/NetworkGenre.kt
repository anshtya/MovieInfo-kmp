package com.anshtya.movieinfo.common.data.network.model.details

import kotlinx.serialization.Serializable

@Serializable
data class NetworkGenre(
    val id: Int,
    val name: String
)