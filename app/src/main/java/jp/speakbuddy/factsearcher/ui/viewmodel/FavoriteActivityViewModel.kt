package jp.speakbuddy.factsearcher.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.speakbuddy.factsearcher.di.DispatchersProvider
import jp.speakbuddy.factsearcher.ui.data.FactUiModel
import jp.speakbuddy.factsearcher.domain.usecase.FavoriteUseCase
import jp.speakbuddy.factsearcher.domain.usecase.UserPreferencesUseCase
import jp.speakbuddy.factsearcher.ui.eventstate.FavoriteUiEvent
import jp.speakbuddy.factsearcher.ui.eventstate.FavoriteUiState
import jp.speakbuddy.factsearcher.ui.utils.LocaleManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class FavoriteActivityViewModel @Inject constructor(
    private val favoriteUseCase: FavoriteUseCase,
    private val dispatchersProvider: DispatchersProvider,
    private val userPreferencesUseCase: UserPreferencesUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow<FavoriteUiState>(FavoriteUiState.Initial)
    val uiState get() = _uiState

    fun onEvent(event: FavoriteUiEvent) {
        when(event){
            is FavoriteUiEvent.GetFavoriteFact -> {
                getFavoriteFactData()
            }
            is FavoriteUiEvent.DislikeFact -> {
               dislikeFact(event.fact)
            }
            is FavoriteUiEvent.UpdatePreferenceLanguage -> {
                updateLanguage(event.locale)
            }
        }
    }

    private fun getFavoriteFactData() {
        viewModelScope.launch(dispatchersProvider.io) {
            favoriteUseCase.getFavoriteFact().also {
                _uiState.tryEmit(FavoriteUiState.Success(it))
            }
        }
    }

    private fun dislikeFact(targetFact: FactUiModel) {
        viewModelScope.launch(dispatchersProvider.io) {
            favoriteUseCase.removeAndReturnFavoriteFact(targetFact).also {
                _uiState.tryEmit(FavoriteUiState.Success(it))
            }
        }
    }

    private fun updateLanguage(locale: Locale) {
        LocaleManager.updateLocale(locale, LocaleManager.PAGE_ID_FAVORITE)

        viewModelScope.launch(dispatchersProvider.io) {
            userPreferencesUseCase.updateLanguagePreference(locale)
        }
    }
}