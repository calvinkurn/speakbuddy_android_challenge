package jp.speakbuddy.factsearcher.utils

import jp.speakbuddy.factsearcher.data.network.FactNetworkModel
import kotlinx.serialization.Serializable

@Serializable
internal data class CatFactResponse (
    val data: List<FactNetworkModel>
)