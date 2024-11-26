package jp.speakbuddy.factsearcher.domain.usecase

import jp.speakbuddy.factsearcher.domain.repository.UserPreferenceRepository
import java.util.Locale
import javax.inject.Inject

class UserPreferencesUseCase @Inject constructor(
    private val userPreferenceRepository: UserPreferenceRepository
) {
    suspend fun updateLanguagePreference(locale: Locale) {
        userPreferenceRepository.setUserPreferenceLanguage(locale)
    }

    fun getLanguagePreference(): Locale {
        return userPreferenceRepository.getUserPreferenceLanguage()
    }
}