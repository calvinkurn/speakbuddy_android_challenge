package jp.speakbuddy.factsearcher.domain.repository

import jp.speakbuddy.factsearcher.network.ApiServices
import jp.speakbuddy.factsearcher.data.network.FactNetworkModel
import javax.inject.Inject

interface FactRepository {
    suspend fun getRandomFact(): FactNetworkModel
}

class FactRepositoryImpl @Inject constructor(
    private val apiServices: ApiServices
): FactRepository {
    override suspend fun getRandomFact(): FactNetworkModel {
        return apiServices.getFact()
    }
}

