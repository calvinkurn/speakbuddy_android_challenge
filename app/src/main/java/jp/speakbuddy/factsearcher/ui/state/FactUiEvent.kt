package jp.speakbuddy.factsearcher.ui.state

import jp.speakbuddy.factsearcher.data.ui.FactUiModel

sealed class FactUiEvent {
    data object CheckFavorite: FactUiEvent()
    data object GetFact: FactUiEvent()
    data object AddFactToFavorite: FactUiEvent()
    data object RestoreSavedFact: FactUiEvent()
    data object SaveFact: FactUiEvent()
}

sealed class FactUiState {
    data object Initial: FactUiState()
    data object Loading: FactUiState()
    data class Error(val error: Throwable): FactUiState()
    data class Success(val data: FactUiModel, val isFavorite: Boolean = false): FactUiState()
    data class FavoriteFact(val isFavorite: Boolean): FactUiState()
}