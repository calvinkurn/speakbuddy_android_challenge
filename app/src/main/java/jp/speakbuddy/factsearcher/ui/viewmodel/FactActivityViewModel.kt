package jp.speakbuddy.factsearcher.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.speakbuddy.factsearcher.data.ui.DefaultFactUiModel
import jp.speakbuddy.factsearcher.domain.error.getErrorMessage
import jp.speakbuddy.factsearcher.domain.usecase.FactUseCase
import jp.speakbuddy.factsearcher.domain.usecase.FavoriteUseCase
import jp.speakbuddy.factsearcher.domain.usecase.SaveDataUseCase
import jp.speakbuddy.factsearcher.domain.usecase.utils.FactResult
import jp.speakbuddy.factsearcher.ui.eventstate.FactUiEvent
import jp.speakbuddy.factsearcher.ui.eventstate.FactUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FactActivityViewModel @Inject constructor(
    private val factUseCase: FactUseCase,
    private val favoriteUseCase: FavoriteUseCase,
    private val saveDataUseCase: SaveDataUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<FactUiState>(FactUiState.Initial)
    val uiState get() = _uiState

    private var factContent = DefaultFactUiModel
    private var isFavorite = false

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
        }
    }

    private fun updateFact() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.tryEmit(FactUiState.Loading)

            factUseCase.getRandomCatFact().let {
                when (it) {
                    is FactResult.Success -> {
                        val newFact = it.data
                        factContent = newFact
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
        viewModelScope.launch(Dispatchers.IO) {
            if (isFavorite) {
                favoriteUseCase.removeFavoriteFact(factContent)
                _uiState.tryEmit(FactUiState.FavoriteFact(false))
            } else {
                favoriteUseCase.addFavoriteFact(factContent)
                _uiState.tryEmit(FactUiState.FavoriteFact(true))
            }

            isFavorite = isFavorite.not()
            saveDataUseCase.saveFactData(factContent)
        }
    }

    private fun restoreLastFact() {
        viewModelScope.launch(Dispatchers.IO) {
            saveDataUseCase.getSavedFactData()?.let { (fact, isFavorite) ->
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

    private fun checkFactFavorite() {
        viewModelScope.launch(Dispatchers.IO) {
            favoriteUseCase.isFactFavorite(factContent).also {
                isFavorite = it
                _uiState.tryEmit(FactUiState.FavoriteFact(it))
            }
        }
    }

    private fun saveLatestFact() {
        viewModelScope.launch {
            saveDataUseCase.saveFactData(factContent)
        }
    }
}
