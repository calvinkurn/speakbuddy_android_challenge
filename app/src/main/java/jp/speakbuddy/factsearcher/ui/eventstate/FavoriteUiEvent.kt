package jp.speakbuddy.factsearcher.ui.eventstate

import jp.speakbuddy.factsearcher.ui.data.FactUiModel

sealed class FavoriteUiEvent {
    data object GetFavoriteFact: FavoriteUiEvent()
    data class DislikeFact(val fact: FactUiModel): FavoriteUiEvent()
}