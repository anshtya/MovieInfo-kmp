package com.anshtya.movieinfo.common.data.util

import java.util.Locale

actual fun getDisplayLanguage(languageCode: String): String {
    return Locale.forLanguageTag(languageCode).displayLanguage
}