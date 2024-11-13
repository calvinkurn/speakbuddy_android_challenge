package jp.speakbuddy.factsearcher.data.network

import jp.speakbuddy.factsearcher.data.ui.FactUiModel
import kotlinx.serialization.Serializable

@Serializable
data class LastFactNetworkModel(
    val fact: FactUiModel,
    val isFavorite: Boolean
)