package jp.speakbuddy.factsearcher.domain.usecase

import jp.speakbuddy.factsearcher.data.ui.FactUiModel
import jp.speakbuddy.factsearcher.domain.repository.DataStoreRepository
import javax.inject.Inject

class FavoriteUseCase @Inject constructor(
    private val favoriteRepository: DataStoreRepository
) {
    suspend fun addFavoriteFact(factData: FactUiModel) {
        favoriteRepository.addFavoriteFact(factData)
    }

    suspend fun removeFavoriteFact(factData: FactUiModel) {
        favoriteRepository.removeFavoriteFact(factData)
    }

    suspend fun getFavoriteFact(): List<FactUiModel> {
        return favoriteRepository.getFavoriteList()
    }
}