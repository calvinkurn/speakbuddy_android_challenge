package jp.speakbuddy.factsearcher.domain.repository

import jp.speakbuddy.factsearcher.network.ApiServices
import jp.speakbuddy.factsearcher.network.model.FactResponse
import javax.inject.Inject

interface FactRepository {
    suspend fun getRandomFact(): FactResponse
}

class FactRepositoryImpl @Inject constructor(
    private val apiServices: ApiServices
): FactRepository {
    override suspend fun getRandomFact(): FactResponse {
        return apiServices.getFact()
    }
}

