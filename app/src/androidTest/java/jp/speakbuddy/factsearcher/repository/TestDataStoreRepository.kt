package jp.speakbuddy.factsearcher.repository

import jp.speakbuddy.factsearcher.domain.repository.DataStoreRepository
import jp.speakbuddy.factsearcher.ui.data.FactUiModel

class TestDataStoreRepository: DataStoreRepository {
    private val favorite: MutableList<FactUiModel> = mutableListOf()

    override suspend fun getFavoriteList(): List<FactUiModel> {
        // expected conversion, need new object to trigger flow to emit new data
        return favorite.toList()
    }

    override suspend fun getSavedLatestFact(): FactUiModel? {
        return null
    }

    override suspend fun saveLatestFact(newData: FactUiModel) {}

    override suspend fun isFactFavorite(fact: FactUiModel): Boolean {
        return favorite.contains(fact)
    }

    override suspend fun removeFavoriteFact(targetData: FactUiModel) {
        favorite.remove(targetData)
    }

    override suspend fun addFavoriteFact(newData: FactUiModel) {
        favorite.add(newData)
    }
}