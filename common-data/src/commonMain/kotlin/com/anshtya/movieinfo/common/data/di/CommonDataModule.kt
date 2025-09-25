package com.anshtya.movieinfo.common.data.di

import com.anshtya.movieinfo.common.data.repository.di.RepositoryModule
import org.koin.core.annotation.Module

@Module(
    includes = [RepositoryModule::class]
)
class CommonDataModule