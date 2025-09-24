package com.anshtya.movieinfo.common.data.util

import platform.Foundation.NSLocale
import platform.Foundation.currentLocale
import platform.Foundation.localizedStringForLanguageCode

actual fun getDisplayLanguage(languageCode: String): String {
    val locale = NSLocale.currentLocale()
    return locale.localizedStringForLanguageCode(languageCode) ?: languageCode
}