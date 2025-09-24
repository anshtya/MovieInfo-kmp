package com.anshtya.movieinfo.common.data.network.model.details.tv

import com.anshtya.movieinfo.common.data.model.details.tv.CreatedBy
import kotlinx.serialization.Serializable

@Serializable
data class NetworkCreatedBy(
    val id: Int,
    val name: String
)

fun NetworkCreatedBy.asModel(): CreatedBy {
    return CreatedBy(
        id = id,
        name = name
    )
}
