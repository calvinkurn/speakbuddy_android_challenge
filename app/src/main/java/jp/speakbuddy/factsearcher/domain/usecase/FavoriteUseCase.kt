package jp.speakbuddy.factsearcher.domain.usecase

import jp.speakbuddy.factsearcher.data.ui.FactUiModel
import jp.speakbuddy.factsearcher.domain.repository.DataStoreRepository
import kotlinx.coroutines.runBlocking
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

    suspend fun removeAndReturnFavoriteFact(factData: FactUiModel): List<FactUiModel> {
        runBlocking {
            dataStoreRepository.removeFavoriteFact(factData)
        }

        return dataStoreRepository.getFavoriteList()
    }

    suspend fun getFavoriteFact(): List<FactUiModel> {
        return dataStoreRepository.getFavoriteList()
    }

    suspend fun isFactFavorite(fact: FactUiModel): Boolean {
        return dataStoreRepository.isFactFavorite(fact)
    }
}