package jp.speakbuddy.factsearcher.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.speakbuddy.factsearcher.data.ui.DefaultCatFactUiModel
import jp.speakbuddy.factsearcher.domain.usecase.FactUseCase
import jp.speakbuddy.factsearcher.domain.usecase.FavoriteUseCase
import jp.speakbuddy.factsearcher.domain.usecase.SaveDataUseCase
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

    private val _factContent = MutableStateFlow(DefaultCatFactUiModel)
    val factContent get() = _factContent

    private val _errorState = MutableStateFlow("")
    val errorState get() = _errorState

    private val _isLoading = MutableStateFlow(false)
    val isLoading get() = _isLoading

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite get() = _isFavorite

    fun updateFact() {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.tryEmit(true)
            try {
                factUseCase.getRandomCatFact().let {
                    _factContent.tryEmit(it)
                    _isFavorite.tryEmit(false)

                    saveDataUseCase.saveFactData(_factContent.value, false)
                }
            } catch (e: Throwable) {
                // TODO: Implement error mapping (BE error -> FE error)
                _errorState.tryEmit(e.message ?: "Unknown")

                "something went wrong. error = ${e.message}"
            }.also {
                _isLoading.tryEmit(false)
            }
        }
    }

    fun addFactToFavorite() {
        viewModelScope.launch(Dispatchers.IO) {
            if (_isFavorite.value) {
                favoriteUseCase.removeFavoriteFact(_factContent.value)
                _isFavorite.tryEmit(false)
            } else {
                favoriteUseCase.addFavoriteFact(_factContent.value)
                _isFavorite.tryEmit(true)
            }

            saveDataUseCase.saveFactData(_factContent.value, _isFavorite.value)
        }
    }

    fun restoreLastFact() {
        viewModelScope.launch(Dispatchers.IO) {
            saveDataUseCase.getSavedFactData()?.let { (fact, isFavorite) ->
                _factContent.tryEmit(fact)
                _isFavorite.tryEmit(isFavorite)
            }
        }
    }
}
