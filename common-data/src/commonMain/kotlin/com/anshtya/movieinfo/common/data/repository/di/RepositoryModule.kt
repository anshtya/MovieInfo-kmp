package com.anshtya.movieinfo.common.data.repository.di

import com.anshtya.movieinfo.common.data.repository.AuthRepository
import com.anshtya.movieinfo.common.data.repository.AuthRepositoryImpl
import com.anshtya.movieinfo.common.data.repository.ContentRepository
import com.anshtya.movieinfo.common.data.repository.ContentRepositoryImpl
import com.anshtya.movieinfo.common.data.repository.DetailsRepository
import com.anshtya.movieinfo.common.data.repository.DetailsRepositoryImpl
import com.anshtya.movieinfo.common.data.repository.LibraryRepository
import com.anshtya.movieinfo.common.data.repository.LibraryRepositoryImpl
import com.anshtya.movieinfo.common.data.repository.SearchRepository
import com.anshtya.movieinfo.common.data.repository.SearchRepositoryImpl
import com.anshtya.movieinfo.common.data.repository.UserRepository
import com.anshtya.movieinfo.common.data.repository.UserRepositoryImpl
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module(
    includes = [ImplModule::class]
)
internal class RepositoryModule {
    @Single
    fun contentRepository(
        impl: ContentRepositoryImpl
    ): ContentRepository {
        return impl
    }

    @Single
    fun searchRepository(
        impl: SearchRepositoryImpl
    ): SearchRepository {
        return impl
    }

    @Single
    fun userRepository(
        impl: UserRepositoryImpl
    ): UserRepository {
        return impl
    }

    @Single
    fun authRepository(
        impl: AuthRepositoryImpl
    ): AuthRepository {
        return impl
    }

    @Single
    fun detailsRepository(
        impl: DetailsRepositoryImpl
    ): DetailsRepository {
        return impl
    }

    @Single
    fun libraryRepository(
        impl: LibraryRepositoryImpl
    ): LibraryRepository {
        return impl
    }
}