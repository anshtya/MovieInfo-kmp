package com.anshtya.movieinfo.common.data.repository

import com.anshtya.movieinfo.common.data.model.details.MovieDetails
import com.anshtya.movieinfo.common.data.model.details.tv.TvSeriesDetails
import com.anshtya.movieinfo.common.data.model.people.PersonDetails
import com.anshtya.movieinfo.common.data.network.TmdbClient
import com.anshtya.movieinfo.common.data.network.model.details.asModel
import com.anshtya.movieinfo.common.data.network.model.details.tv.asModel
import org.koin.core.annotation.Single

@Single
internal class DetailsRepositoryImpl(
    private val tmdbClient: TmdbClient
) : DetailsRepository {
    override suspend fun getMovieDetails(id: Int): Result<MovieDetails> {
        return runCatching {
            tmdbClient.getMovieDetails(id).getOrThrow().asModel()
        }
    }

    override suspend fun getTvSeriesDetails(id: Int): Result<TvSeriesDetails> {
        return runCatching {
            tmdbClient.getTvSeriesDetails(id).getOrThrow().asModel()
        }
    }

    override suspend fun getPersonDetails(id: Int): Result<PersonDetails> {
        return runCatching {
            tmdbClient.getPersonDetails(id).getOrThrow().asModel()
        }
    }
}