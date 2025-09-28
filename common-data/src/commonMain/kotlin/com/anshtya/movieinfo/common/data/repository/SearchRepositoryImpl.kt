package com.anshtya.movieinfo.common.data.repository

import com.anshtya.movieinfo.common.data.model.SearchItem
import com.anshtya.movieinfo.common.data.network.TmdbClient
import com.anshtya.movieinfo.common.data.network.model.search.asModel
import org.koin.core.annotation.Single

@Single
internal class SearchRepositoryImpl(
    private val tmdbClient: TmdbClient
) : SearchRepository {
    override suspend fun getSearchSuggestions(
        query: String,
        includeAdult: Boolean
    ): Result<List<SearchItem>> {
        return tmdbClient.multiSearch(
            query = query,
            includeAdult = includeAdult
        ).map { it.results.map { item -> item.asModel() } }
    }
}