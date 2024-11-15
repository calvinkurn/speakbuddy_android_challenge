package jp.speakbuddy.factsearcher.ui.eventstate

sealed class FactUiEvent {
    data object CheckFavorite: FactUiEvent()
    data object GetFact: FactUiEvent()
    data object AddFactToFavorite: FactUiEvent()
    data object RestoreSavedFact: FactUiEvent()
    data object SaveFact: FactUiEvent()
}