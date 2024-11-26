package jp.speakbuddy.factsearcher.data.network

import kotlinx.serialization.Serializable

@Serializable
data class FactNetworkModel(
    val fact: String,
    val length: Int
)
