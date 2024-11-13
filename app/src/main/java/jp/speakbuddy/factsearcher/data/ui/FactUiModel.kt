package jp.speakbuddy.factsearcher.data.ui

import kotlinx.serialization.Serializable

@Serializable
data class FactUiModel (
    val fact: String,
    val length: Int,
    val isContainsCat: Boolean
) {

    /**
     * True -> all value is identical
     * False -> some value is different
     */
    fun isEqual(compare: FactUiModel): Boolean {
        return compare.fact == fact && compare.length == length && compare.isContainsCat == isContainsCat
    }
}

val DefaultCatFactUiModel get() = FactUiModel(
    fact = "",
    length = 0,
    isContainsCat = false
)