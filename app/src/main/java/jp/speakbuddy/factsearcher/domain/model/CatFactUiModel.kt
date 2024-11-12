package jp.speakbuddy.factsearcher.domain.model

data class CatFactUiModel (
    val fact: String,
    val length: Int,
    val isContainsCat: Boolean
)

val DefaultCatFactUiModel get() = CatFactUiModel(
    fact = "",
    length = 0,
    isContainsCat = false
)