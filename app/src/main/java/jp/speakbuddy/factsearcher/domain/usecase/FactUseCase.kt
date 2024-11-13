package jp.speakbuddy.factsearcher.domain.usecase

import jp.speakbuddy.factsearcher.data.ui.FactUiModel
import jp.speakbuddy.factsearcher.domain.repository.FactRepository
import java.util.Locale
import javax.inject.Inject

class FactUseCase @Inject constructor(
    private val factRepository: FactRepository
) {
    suspend fun getRandomCatFact(): FactUiModel {
        return factRepository.getRandomFact().let {
            FactUiModel(
                fact = it.fact,
                length = it.length,
                isContainsCat = it.fact.lowercase(Locale.US).contains("cats")
            )
        }
    }
}