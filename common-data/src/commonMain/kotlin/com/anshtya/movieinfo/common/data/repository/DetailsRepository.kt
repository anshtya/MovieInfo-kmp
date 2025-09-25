package com.anshtya.movieinfo.common.data.repository

import com.anshtya.movieinfo.common.data.model.details.MovieDetails
import com.anshtya.movieinfo.common.data.model.details.tv.TvSeriesDetails
import com.anshtya.movieinfo.common.data.model.people.PersonDetails

interface DetailsRepository {
    suspend fun getMovieDetails(id: Int): Result<MovieDetails>
    suspend fun getTvSeriesDetails(id: Int): Result<TvSeriesDetails>
    suspend fun getPersonDetails(id: Int): Result<PersonDetails>
}