package jp.speakbuddy.factsearcher.ui.eventstate

import jp.speakbuddy.factsearcher.data.ui.FactUiModel

sealed class FavoriteUiEvent {
    data object GetFavoriteFact: FavoriteUiEvent()
    data class DislikeFact(val fact: FactUiModel): FavoriteUiEvent()
}