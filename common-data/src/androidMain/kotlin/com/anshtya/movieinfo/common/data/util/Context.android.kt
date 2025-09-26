package com.anshtya.movieinfo.common.data.util

import android.content.Context
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import org.koin.core.scope.Scope

actual class ContextWrapper(val context: Context)

@Module
internal actual class ContextModule actual constructor() {
    @Single
    actual fun providesContextWrapper(scope: Scope): ContextWrapper {
        return ContextWrapper(scope.get())
    }
}