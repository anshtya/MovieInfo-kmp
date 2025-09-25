package com.anshtya.movieinfo.common.data.repository

import com.anshtya.movieinfo.common.data.model.Content
import com.anshtya.movieinfo.common.data.model.category.MovieListCategory
import com.anshtya.movieinfo.common.data.model.category.TvSeriesListCategory


interface ContentRepository {
    suspend fun getMovieItems(
        page: Int,
        category: MovieListCategory
    ): Result<List<Content>>

    suspend fun getTvSeriesItems(
        page: Int,
        category: TvSeriesListCategory
    ): Result<List<Content>>
}