package jp.speakbuddy.factsearcher.domain.usecase

import jp.speakbuddy.factsearcher.data.ui.FactUiModel
import jp.speakbuddy.factsearcher.domain.repository.DataStoreRepository
import javax.inject.Inject

class FavoriteUseCase @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) {
    suspend fun addFavoriteFact(factData: FactUiModel) {
        dataStoreRepository.addFavoriteFact(factData)
    }

    suspend fun removeFavoriteFact(factData: FactUiModel) {
        dataStoreRepository.removeFavoriteFact(factData)
    }

    suspend fun getFavoriteFact(): List<FactUiModel> {
        return dataStoreRepository.getFavoriteList()
    }
}