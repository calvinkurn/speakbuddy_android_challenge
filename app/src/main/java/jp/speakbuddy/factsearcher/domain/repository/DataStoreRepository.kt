package jp.speakbuddy.factsearcher.domain.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import jp.speakbuddy.factsearcher.ui.data.FactUiModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

interface DataStoreRepository {
    suspend fun getFavoriteList(): List<FactUiModel>
    suspend fun addFavoriteFact(newData: FactUiModel)
    suspend fun removeFavoriteFact(targetData: FactUiModel)
    suspend fun isFactFavorite(fact: FactUiModel): Boolean

    suspend fun getSavedLatestFact(): FactUiModel?
    suspend fun saveLatestFact(newData: FactUiModel)
}

class DataStoreRepositoryImpl @Inject constructor(
    @ApplicationContext context: Context
) : DataStoreRepository {
    private val favoriteDataKey = stringPreferencesKey("favorite_fact_list")
    private val latestDataKey = stringPreferencesKey("latest_fact")
    private val dataStore = context.dataStore

    override suspend fun addFavoriteFact(newData: FactUiModel) {
        val currentList = getFavoriteList()
        val newList = currentList + listOf(newData)

        writeFavoriteFactToStorage(newList)
    }

    override suspend fun removeFavoriteFact(targetData: FactUiModel) {
        val newList = getFavoriteList().filter {
            it.factId != targetData.factId
        }

        writeFavoriteFactToStorage(newList)
    }

    override suspend fun getFavoriteList(): List<FactUiModel> {
        return dataStore.data.map {
            it[favoriteDataKey]
        }.firstOrNull()?.let {
            Json.decodeFromString(ListSerializer(FactUiModel.serializer()), it)
        } ?: emptyList()
    }

    override suspend fun getSavedLatestFact(): FactUiModel? {
        return dataStore.data.map {
            it[latestDataKey]
        }.firstOrNull()?.let {
            Json.decodeFromString(it)
        }
    }

    override suspend fun saveLatestFact(newData: FactUiModel) {
        dataStore.edit {
            it[latestDataKey] = Json.encodeToString(newData)
        }
    }

    override suspend fun isFactFavorite(fact: FactUiModel): Boolean {
        return getFavoriteList().contains(fact)
    }

    private suspend fun writeFavoriteFactToStorage(newList: List<FactUiModel>) {
        dataStore.edit {
            it[favoriteDataKey] = Json.encodeToString(ListSerializer(FactUiModel.serializer()), newList)
        }
    }

    companion object {
        private const val PREFERENCE_NAME = "fact_storage_data"
        val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFERENCE_NAME)
    }
}