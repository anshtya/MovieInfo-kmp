package com.anshtya.movieinfo.common.data.repository.di

import com.anshtya.movieinfo.common.data.repository.LibraryRepositoryImpl
import com.anshtya.movieinfo.common.data.workscheduler.LibrarySyncManager
import com.anshtya.movieinfo.common.data.workscheduler.LibraryWorkExecutor
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module(
    includes = [ImplModule::class]
)
internal class LibraryImplModule {
    @Single
    fun libraryWorkExecutor(
        impl: LibraryRepositoryImpl
    ): LibraryWorkExecutor {
        return impl
    }

    @Single
    fun librarySyncManager(
        impl: LibraryRepositoryImpl
    ): LibrarySyncManager {
        return impl
    }
}