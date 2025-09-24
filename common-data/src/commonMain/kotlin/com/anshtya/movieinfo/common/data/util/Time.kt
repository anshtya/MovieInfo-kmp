package com.anshtya.movieinfo.common.data.util

import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char

internal fun getFormattedRuntime(runtime: Int): String {
    val hours = runtime.div(60)
    val minutes = runtime.mod(60)
    return if (hours < 1) {
        "${minutes}m"
    } else if (minutes < 1) {
        "${hours}h"
    } else {
        "${hours}h ${minutes}m"
    }
}

internal fun formatDateString(dateString: String): String {
    val outputFormat = LocalDate.Format {
        day(Padding.NONE)
        char(' ')
        monthName(MonthNames.ENGLISH_FULL)
        char(' ')
        year()
    }

    val date = LocalDate.parse(dateString)
    return date.format(outputFormat)
}