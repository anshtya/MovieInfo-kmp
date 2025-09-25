package com.anshtya.movieinfo.common.data.repository.test

import com.anshtya.movieinfo.common.data.model.details.MovieDetails
import com.anshtya.movieinfo.common.data.model.details.tv.TvSeriesDetails
import com.anshtya.movieinfo.common.data.model.people.PersonDetails
import com.anshtya.movieinfo.common.data.repository.DetailsRepository
import com.anshtya.movieinfo.common.data.repository.test.data.testMovieDetail
import com.anshtya.movieinfo.common.data.repository.test.data.testPersonDetails
import com.anshtya.movieinfo.common.data.repository.test.data.testTvSeriesDetails

class TestDetailsRepository : DetailsRepository {
    private var generateError = false

    override suspend fun getMovieDetails(id: Int): Result<MovieDetails> {
        return if (!generateError) {
            Result.success(testMovieDetail)
        } else {
            Result.failure(Exception("error"))
        }
    }

    override suspend fun getTvSeriesDetails(id: Int): Result<TvSeriesDetails> {
        return if (!generateError) {
            Result.success(testTvSeriesDetails)
        } else {
            Result.failure(Exception("error"))
        }
    }

    override suspend fun getPersonDetails(id: Int): Result<PersonDetails> {
        return if (!generateError) {
            Result.success(testPersonDetails)
        } else {
            Result.failure(Exception("error"))
        }
    }

    fun generateError(value: Boolean) {
        generateError = value
    }
}