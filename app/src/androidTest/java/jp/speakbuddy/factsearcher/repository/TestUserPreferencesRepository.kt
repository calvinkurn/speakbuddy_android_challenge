package jp.speakbuddy.factsearcher.repository

import jp.speakbuddy.factsearcher.domain.repository.UserPreferenceRepository
import java.util.Locale

class TestUserPreferencesRepository: UserPreferenceRepository {
    override suspend fun getUserPreferenceLanguage(): Locale {
        return Locale.US
    }

    override suspend fun setUserPreferenceLanguage(locale: Locale) {}
}