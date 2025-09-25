package com.anshtya.movieinfo.common.data.util

import platform.Foundation.NSNumber
import platform.Foundation.NSNumberFormatter
import platform.Foundation.NSNumberFormatterDecimalStyle

internal actual fun formatCurrency(number: Int): String {
    val formatter = NSNumberFormatter().apply {
        numberStyle = NSNumberFormatterDecimalStyle
    }
    return formatter.stringFromNumber(NSNumber(number)) ?: number.toString()
}