package jp.speakbuddy.factsearcher.ui.eventstate

import jp.speakbuddy.factsearcher.ui.data.FactUiModel

sealed class FactUiState {
    data object Initial: FactUiState()
    data object Loading: FactUiState()
    data class Error(val errorMsg: String): FactUiState()
    data class Success(val data: FactUiModel, val isFavorite: Boolean = false): FactUiState()
    data class FavoriteFact(val isFavorite: Boolean): FactUiState()
}