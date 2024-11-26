package jp.speakbuddy.factsearcher.ui.eventstate

import jp.speakbuddy.factsearcher.ui.data.FactUiModel
import java.util.Locale

sealed class FavoriteUiEvent {
    data object GetFavoriteFact: FavoriteUiEvent()
    data class DislikeFact(val fact: FactUiModel): FavoriteUiEvent()
    data class UpdatePreferenceLanguage(val locale: Locale): FavoriteUiEvent()
}