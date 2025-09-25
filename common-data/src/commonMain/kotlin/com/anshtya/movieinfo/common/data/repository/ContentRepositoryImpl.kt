package com.anshtya.movieinfo.common.data.repository

import com.anshtya.movieinfo.common.data.local.database.dao.AccountDetailsDao
import com.anshtya.movieinfo.common.data.model.Content
import com.anshtya.movieinfo.common.data.model.category.MovieListCategory
import com.anshtya.movieinfo.common.data.model.category.TvSeriesListCategory
import com.anshtya.movieinfo.common.data.network.TmdbClient
import com.anshtya.movieinfo.common.data.network.model.asModel

internal class ContentRepositoryImpl(
    private val tmdbClient: TmdbClient,
    private val accountDetailsDao: AccountDetailsDao
) : ContentRepository {
    override suspend fun getMovieItems(
        page: Int,
        category: MovieListCategory
    ): Result<List<Content>> {
        return runCatching {
            val response = tmdbClient.getMovieLists(
                category = category.categoryName,
                page = page,
                region = accountDetailsDao.getRegionCode()
            ).getOrThrow()

            response.results.map { it.asModel() }
        }
    }

    override suspend fun getTvSeriesItems(
        page: Int,
        category: TvSeriesListCategory
    ): Result<List<Content>> {
        return runCatching {
            val response = tmdbClient.getTvShowLists(
                category = category.categoryName,
                page = page
            ).getOrThrow()

            response.results.map { it.asModel() }
        }
    }
}