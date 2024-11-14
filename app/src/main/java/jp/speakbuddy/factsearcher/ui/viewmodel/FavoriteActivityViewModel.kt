package jp.speakbuddy.factsearcher.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.speakbuddy.factsearcher.data.ui.FactUiModel
import jp.speakbuddy.factsearcher.domain.usecase.FavoriteUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteActivityViewModel @Inject constructor(
    private val favoriteUseCase: FavoriteUseCase
): ViewModel() {
    private val _favoriteFactData = MutableStateFlow<List<FactUiModel>>(listOf())
    val favoriteFactData get() = _favoriteFactData

    fun getFavoriteFactData() {
        viewModelScope.launch(Dispatchers.IO) {
            favoriteUseCase.getFavoriteFact().also {
                _favoriteFactData.tryEmit(it)
            }
        }
    }

    fun dislikeFact(targetFact: FactUiModel) {
        viewModelScope.launch(Dispatchers.IO) {
            favoriteUseCase.removeAndReturnFavoriteFact(targetFact).also {
                _favoriteFactData.tryEmit(it)
            }
        }
    }
}