package com.anshtya.movieinfo.di

import com.anshtya.movieinfo.common.data.di.CommonDataModule
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module(
    includes = [CommonDataModule::class]
)
@ComponentScan("com.anshtya.movieinfo")
class AppModule