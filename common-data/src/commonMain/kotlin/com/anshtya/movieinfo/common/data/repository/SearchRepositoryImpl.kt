package com.anshtya.movieinfo.common.data.repository

import com.anshtya.movieinfo.common.data.model.SearchItem
import com.anshtya.movieinfo.common.data.network.TmdbClient
import com.anshtya.movieinfo.common.data.network.model.search.asModel

internal class SearchRepositoryImpl(
    private val tmdbClient: TmdbClient
) : SearchRepository {
    override suspend fun getSearchSuggestions(
        query: String,
        includeAdult: Boolean
    ): Result<List<SearchItem>> {
        return runCatching {
            val result = tmdbClient.multiSearch(
                query = query,
                includeAdult = includeAdult
            ).getOrThrow()

            result.results.map { it.asModel() }
        }
    }
}