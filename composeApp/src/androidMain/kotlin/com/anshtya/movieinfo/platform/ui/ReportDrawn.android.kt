package com.anshtya.movieinfo.platform.ui

import androidx.compose.runtime.Composable

@Composable
actual fun ReportDrawnWhen(
    predicate: () -> Boolean
) {
    androidx.activity.compose.ReportDrawnWhen(predicate = predicate)
}