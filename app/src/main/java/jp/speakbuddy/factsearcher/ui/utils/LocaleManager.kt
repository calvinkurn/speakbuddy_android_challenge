package jp.speakbuddy.factsearcher.ui.utils

import androidx.activity.ComponentActivity
import jp.speakbuddy.factsearcher.ui.view.FactActivity
import java.util.Locale

/**
 * Need to control all page locale since we have language feature on all pages
 */
object LocaleManager {
    private var currentLocale: Locale = Locale.US
    private val localeState: MutableMap<Int, Locale> = mutableMapOf()

    fun checkPageLocale(activity: ComponentActivity) {
        val pageId = when(activity) {
            is FactActivity -> PAGE_ID_FACT
            else -> PAGE_ID_FAVORITE
        }

        if (currentLocale != localeState.getOrDefault(pageId, currentLocale)) {
            localeState[pageId] = currentLocale
            activity.recreate()
        }
    }

    fun updateLocale(locale: Locale, pageId: Int) {
        currentLocale = locale
        localeState[pageId] = locale
    }

    const val PAGE_ID_FACT = 135
    const val PAGE_ID_FAVORITE = 246
}