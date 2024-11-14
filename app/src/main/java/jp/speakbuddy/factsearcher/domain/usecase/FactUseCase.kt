package jp.speakbuddy.factsearcher.domain.usecase

import jp.speakbuddy.factsearcher.data.ui.FactUiModel
import jp.speakbuddy.factsearcher.domain.repository.FactRepository
import java.util.Locale
import javax.inject.Inject

class FactUseCase @Inject constructor(
    private val factRepository: FactRepository
) {
    suspend fun getRandomCatFact(): Result<FactUiModel> {
        try {
            factRepository.getRandomFact().let {
                return Result.success(
                    FactUiModel(
                        factId = System.currentTimeMillis(),
                        fact = it.fact,
                        length = it.length,
                        isContainsCat = it.fact.lowercase(Locale.US).contains("cats")
                    )
                )
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}