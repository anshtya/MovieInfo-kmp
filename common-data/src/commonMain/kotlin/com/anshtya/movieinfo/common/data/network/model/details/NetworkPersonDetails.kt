package com.anshtya.movieinfo.common.data.network.model.details

import com.anshtya.movieinfo.common.data.model.people.PersonDetails
import com.anshtya.movieinfo.common.data.util.formatDateString
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkPersonDetails(
    val adult: Boolean,
    @SerialName("also_known_as") val alsoKnownAs: List<String>,
    val biography: String?,
    val birthday: String?,
    val deathday: String?,
    val gender: Int,
    val id: Int,
    @SerialName("known_for_department") val knownForDepartment: String,
    val name: String,
    @SerialName("place_of_birth") val placeOfBirth: String?,
    @SerialName("profile_path") val profilePath: String?
) {
    // According to https://developer.themoviedb.org/reference/person-details#genders
    fun getGender(): String {
        return when (gender) {
            1 -> "Female"
            2 -> "Male"
            3 -> "Non-binary"
            else -> "Not specified"
        }
    }
}

fun NetworkPersonDetails.asModel(): PersonDetails {
    return PersonDetails(
        adult = adult,
        alsoKnownAs = if (alsoKnownAs.isEmpty()) "Unknown" else alsoKnownAs.joinToString(", "),
        biography = if (biography.isNullOrEmpty()) "Not available" else biography,
        birthday = birthday?.let { formatDateString(it) } ?: "Unknown",
        deathday = deathday?.let { formatDateString(it) },
        gender = getGender(),
        id = id,
        knownForDepartment = knownForDepartment,
        name = name,
        placeOfBirth = placeOfBirth ?: "Unknown",
        profilePath = profilePath ?: ""
    )
}