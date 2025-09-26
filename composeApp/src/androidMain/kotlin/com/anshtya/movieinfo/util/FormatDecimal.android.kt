package com.anshtya.movieinfo.util

actual fun formatRating(
    rating: Float,
    decimals: Int
): String {
    return String.format("%.${decimals}f", rating)
}