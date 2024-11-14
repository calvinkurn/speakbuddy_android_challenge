package jp.speakbuddy.factsearcher.data.ui

import kotlinx.serialization.Serializable

/**
 * Note:
 * ID is not provided by BE, ID is created on FE and some data content may be duplicated with different factID
 */
@Serializable
data class FactUiModel (
    val factId: Long = 0L,
    val fact: String,
    val length: Int,
    val isContainsCat: Boolean
)

val DefaultFactUiModel get() = FactUiModel(
    factId = 0,
    fact = "",
    length = 0,
    isContainsCat = false
)