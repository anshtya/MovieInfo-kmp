package com.anshtya.movieinfo.common.data.repository.test

import com.anshtya.movieinfo.common.data.model.SearchItem
import com.anshtya.movieinfo.common.data.repository.SearchRepository
import com.anshtya.movieinfo.common.data.repository.test.data.testSearchResults

class TestSearchRepository: SearchRepository {
    private var generateError = false

    override suspend fun getSearchSuggestions(
        query: String,
        includeAdult: Boolean
    ): Result<List<SearchItem>> {
        if (generateError) return Result.failure(Exception("error"))

        return Result.success(testSearchResults)
    }

    fun generateError(value: Boolean) {
        generateError = value
    }
}