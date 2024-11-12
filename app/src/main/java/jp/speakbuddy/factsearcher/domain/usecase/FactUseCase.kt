package jp.speakbuddy.factsearcher.domain.usecase

import jp.speakbuddy.factsearcher.domain.model.CatFactUiModel
import jp.speakbuddy.factsearcher.domain.repository.FactRepository
import java.util.Locale
import javax.inject.Inject

class FactUseCase @Inject constructor(
    private val factRepository: FactRepository
) {
    suspend fun getRandomCatFact():  CatFactUiModel{
        return factRepository.getRandomFact().let {
            CatFactUiModel(
                fact = it.fact,
                length = it.length,
                isContainsCat = it.fact.lowercase(Locale.US).contains("cats")
            )
        }
    }
}