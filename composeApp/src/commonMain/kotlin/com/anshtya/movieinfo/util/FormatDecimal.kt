package com.anshtya.movieinfo.util

expect fun formatRating(
    rating: Float,
    decimals: Int = 1
): String