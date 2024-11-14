package jp.speakbuddy.factsearcher.domain.usecase

import jp.speakbuddy.factsearcher.data.network.LastFactNetworkModel
import jp.speakbuddy.factsearcher.data.ui.FactUiModel
import jp.speakbuddy.factsearcher.domain.repository.DataStoreRepository
import javax.inject.Inject

class SaveDataUseCase @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) {
    suspend fun saveFactData(latestFact: FactUiModel, isFavorite: Boolean) {
        dataStoreRepository.saveLatestFact(
            LastFactNetworkModel(
                fact = latestFact,
                isFavorite = isFavorite
            )
        )
    }

    // return latest fact & favorite status
    suspend fun getSavedFactData(): Pair<FactUiModel, Boolean>? {
        val data = dataStoreRepository.getSavedLatestFact()
        return data?.let {
            Pair(it.fact, it.isFavorite)
        }
    }
}