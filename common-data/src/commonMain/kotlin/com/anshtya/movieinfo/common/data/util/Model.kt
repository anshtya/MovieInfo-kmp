package com.anshtya.movieinfo.common.data.util

import com.anshtya.movieinfo.common.data.local.database.entity.AccountDetailsEntity
import com.anshtya.movieinfo.common.data.network.model.auth.NetworkAccountDetails

fun NetworkAccountDetails.asEntity(): AccountDetailsEntity {
    return AccountDetailsEntity(
        id = id,
        name = name,
        username = username,
        includeAdult = includeAdult,
        iso6391 = iso6391,
        iso31661 = iso31661,
        gravatarHash = avatar.gravatar.hash,
        tmdbAvatarPath = avatar.tmdb.avatarPath
    )
}