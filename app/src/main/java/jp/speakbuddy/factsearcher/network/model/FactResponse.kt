package jp.speakbuddy.factsearcher.network.model

import kotlinx.serialization.Serializable

@Serializable
data class FactResponse(
    val fact: String,
    val length: Int
)
