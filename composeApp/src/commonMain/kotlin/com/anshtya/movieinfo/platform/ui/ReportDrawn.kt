package com.anshtya.movieinfo.platform.ui

import androidx.compose.runtime.Composable

@Composable
expect fun ReportDrawnWhen(
    predicate: () -> Boolean
)