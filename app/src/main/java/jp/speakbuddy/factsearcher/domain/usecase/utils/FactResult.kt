package jp.speakbuddy.factsearcher.domain.usecase.utils

import jp.speakbuddy.factsearcher.data.ui.FactUiModel
import jp.speakbuddy.factsearcher.domain.error.ErrorType

sealed class FactResult<out T> {
    data class Success<out T>(val data: FactUiModel): FactResult<T>()
    data class Failure(val error: ErrorType): FactResult<Nothing>()
}