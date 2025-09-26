package com.anshtya.movieinfo.util

import platform.Foundation.NSString
import platform.Foundation.stringWithFormat

actual fun formatRating(
    rating: Float,
    decimals: Int
): String {
    val format = "%.${decimals}f"
    return NSString.stringWithFormat(format, rating)
}