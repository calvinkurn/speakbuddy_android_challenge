package jp.speakbuddy.factsearcher.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.speakbuddy.factsearcher.di.DispatchersProvider
import jp.speakbuddy.factsearcher.ui.data.DefaultFactUiModel
import jp.speakbuddy.factsearcher.domain.error.getErrorMessage
import jp.speakbuddy.factsearcher.domain.usecase.FactUseCase
import jp.speakbuddy.factsearcher.domain.usecase.FavoriteUseCase
import jp.speakbuddy.factsearcher.domain.usecase.SaveDataUseCase
import jp.speakbuddy.factsearcher.domain.usecase.UserPreferencesUseCase
import jp.speakbuddy.factsearcher.domain.usecase.utils.FactResult
import jp.speakbuddy.factsearcher.ui.eventstate.FactUiEvent
import jp.speakbuddy.factsearcher.ui.eventstate.FactUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class FactActivityViewModel @Inject constructor(
    private val factUseCase: FactUseCase,
    private val favoriteUseCase: FavoriteUseCase,
    private val saveDataUseCase: SaveDataUseCase,
    private val dispatchersProvider: DispatchersProvider,
    private val userPreferencesUseCase: UserPreferencesUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<FactUiState>(FactUiState.Initial)
    val uiState get() = _uiState

    private var factContent = DefaultFactUiModel
    private var isFavorite = false

    fun getLanguagePreference(): Locale {
        return userPreferencesUseCase.getLanguagePreference()
    }

    fun onEvent(event: FactUiEvent) {
        when (event) {
            is FactUiEvent.GetFact -> {
                updateFact()
            }
            is FactUiEvent.CheckFavorite -> {
                checkFactFavorite()
            }
            is FactUiEvent.AddFactToFavorite -> {
                addFactToFavorite()
            }
            is FactUiEvent.RestoreSavedFact -> {
                restoreLastFact()
            }
            is FactUiEvent.SaveFact -> {
                saveLatestFact()
            }
            is FactUiEvent.UpdatePreferenceLanguage -> {
                updateLanguage(event.locale)
            }
        }
    }

    private fun updateFact() {
        viewModelScope.launch(dispatchersProvider.io) {
            _uiState.tryEmit(FactUiState.Loading)

            factUseCase.getRandomCatFact().let {
                when (it) {
                    is FactResult.Success -> {
                        val newFact = it.data
                        factContent = newFact
                        isFavorite = false
                        _uiState.tryEmit(FactUiState.Success(newFact))
                    }
                    is FactResult.Failure -> {
                        _uiState.tryEmit(FactUiState.Error(getErrorMessage(it.error)))
                    }
                }
            }
        }
    }

    private fun addFactToFavorite() {
        viewModelScope.launch(dispatchersProvider.io) {
            if (isFavorite) {
                favoriteUseCase.removeFavoriteFact(factContent)
                _uiState.tryEmit(FactUiState.FavoriteFact(false))
            } else {
                favoriteUseCase.addFavoriteFact(factContent)
                _uiState.tryEmit(FactUiState.FavoriteFact(true))
            }

            isFavorite = isFavorite.not()
        }
    }

    private fun restoreLastFact() {
        if (factContent.fact != DefaultFactUiModel.fact) {
            // on configuration change
            _uiState.tryEmit(FactUiState.Success(factContent, isFavorite))
        } else {
            viewModelScope.launch(dispatchersProvider.io) {
                _uiState.tryEmit(FactUiState.Loading)

                saveDataUseCase.getSavedFactData().let { (fact, isFavorite) ->
                    if (fact.fact.isEmpty()) {
                        updateFact()
                    } else {
                        factContent = fact
                        _uiState.tryEmit(FactUiState.Success(fact, isFavorite))
                        this@FactActivityViewModel.isFavorite = isFavorite
                    }
                }
            }
        }
    }

    private fun checkFactFavorite() {
        viewModelScope.launch(dispatchersProvider.io) {
            if (factContent != DefaultFactUiModel) {
                favoriteUseCase.isFactFavorite(factContent).also {
                    isFavorite = it
                    _uiState.tryEmit(FactUiState.FavoriteFact(it))
                }
            }
        }
    }

    private fun saveLatestFact() {
        viewModelScope.launch(dispatchersProvider.io) {
            saveDataUseCase.saveFactData(factContent)
        }
    }

    private fun updateLanguage(locale: Locale) {
        viewModelScope.launch(dispatchersProvider.io) {
            userPreferencesUseCase.updateLanguagePreference(locale)
        }
    }
}
