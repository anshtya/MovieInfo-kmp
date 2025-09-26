package com.anshtya.movieinfo.di

import com.anshtya.movieinfo.common.data.di.CommonDataModule
import org.koin.core.annotation.Module

@Module(
    includes = [CommonDataModule::class]
)
class AppModule