package com.anshtya.movieinfo.common.data.repository

import com.anshtya.movieinfo.common.data.model.SearchItem

interface SearchRepository {
    suspend fun getSearchSuggestions(
        query: String,
        includeAdult: Boolean
    ): Result<List<SearchItem>>
}