package com.anshtya.movieinfo.common.data.util

import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import org.koin.core.scope.Scope

actual class ContextWrapper

@Module
internal actual class ContextModule {
    @Single
    actual fun providesContextWrapper(scope: Scope): ContextWrapper {
        return ContextWrapper()
    }
}