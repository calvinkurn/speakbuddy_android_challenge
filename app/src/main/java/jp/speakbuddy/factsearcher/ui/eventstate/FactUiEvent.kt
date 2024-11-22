package jp.speakbuddy.factsearcher.ui.eventstate

import java.util.Locale

sealed class FactUiEvent {
    data object CheckFavorite: FactUiEvent()
    data object GetFact: FactUiEvent()
    data object AddFactToFavorite: FactUiEvent()
    data object RestoreSavedFact: FactUiEvent()
    data object SaveFact: FactUiEvent()

    data class UpdatePreferenceLanguage(val locale: Locale): FactUiEvent()
}