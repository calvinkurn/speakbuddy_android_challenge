package jp.speakbuddy.factsearcher.ui.eventstate

import jp.speakbuddy.factsearcher.ui.data.FactUiModel

sealed class FavoriteUiState {
    data class Success(val favoriteFactList: List<FactUiModel>): FavoriteUiState()
    data object Initial: FavoriteUiState()
}