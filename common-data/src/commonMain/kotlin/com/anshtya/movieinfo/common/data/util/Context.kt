package com.anshtya.movieinfo.common.data.util

import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import org.koin.core.scope.Scope

internal expect class ContextWrapper

@Module
internal expect class ContextModule() {
    @Single
    fun providesContextWrapper(scope : Scope) : ContextWrapper
}