package jp.speakbuddy.factsearcher.domain.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import java.util.Locale
import javax.inject.Inject

interface UserPreferenceRepository {
    suspend fun setUserPreferenceLanguage(locale: Locale)
    fun getUserPreferenceLanguage(): Locale
}

class UserPreferenceRepositoryImpl @Inject constructor(
    @ApplicationContext context: Context
) : UserPreferenceRepository {
    private val localeKey = stringPreferencesKey(SELECTED_LANGUAGE)
    private val dataStore = context.dataStore

    override fun getUserPreferenceLanguage(): Locale {
        return runBlocking {
            dataStore.data.map {
                it[localeKey]
            }.firstOrNull()?.let {
                Locale.forLanguageTag(it)
            } ?: Locale.US
        }
    }

    override suspend fun setUserPreferenceLanguage(locale: Locale) {
        dataStore.edit {
            it[localeKey] = locale.toLanguageTag()
        }
    }

    companion object {
        private const val PREFERENCE_NAME = "user_language_preferences"
        private const val SELECTED_LANGUAGE = "selected_language"

        val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFERENCE_NAME)
    }
}