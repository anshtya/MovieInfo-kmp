package com.anshtya.movieinfo.common.data.util

import java.util.Locale

internal actual fun formatCurrency(number: Int): String {
    return String.format(Locale.getDefault(),"%,d", number)
}