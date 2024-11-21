package jp.speakbuddy.factsearcher.utils

import android.content.Context
import java.util.Locale

fun Context.setLocale(locale: Locale): Context {
    Locale.setDefault(locale)
    val config = resources.configuration
    config.setLocale(locale)
    return createConfigurationContext(config)
}

fun Context.getLocalizedString(resourceId: Int): String {
    return resources.getString(resourceId)
}