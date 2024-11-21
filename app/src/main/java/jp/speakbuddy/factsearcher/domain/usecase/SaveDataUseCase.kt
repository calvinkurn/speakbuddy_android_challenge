package jp.speakbuddy.factsearcher.domain.usecase

import jp.speakbuddy.factsearcher.ui.data.FactUiModel
import jp.speakbuddy.factsearcher.domain.repository.DataStoreRepository
import javax.inject.Inject

class SaveDataUseCase @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) {
    suspend fun saveFactData(latestFact: FactUiModel) {
        dataStoreRepository.saveLatestFact(latestFact)
    }

    // return latest fact & favorite status
    suspend fun getSavedFactData(): Pair<FactUiModel, Boolean> {
        val data = dataStoreRepository.getSavedLatestFact()
        return data?.let {
            val isFavorite = dataStoreRepository.isFactFavorite(it)
            Pair(it, isFavorite)
        } ?: run {
            Pair(FactUiModel(
                fact = "",
                factId = 0L,
                length = 0,
                isContainsCat = false
            ), false)
        }
    }
}