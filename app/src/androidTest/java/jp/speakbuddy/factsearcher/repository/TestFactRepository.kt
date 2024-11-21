package jp.speakbuddy.factsearcher.repository

import jp.speakbuddy.factsearcher.data.network.FactNetworkModel
import jp.speakbuddy.factsearcher.domain.repository.FactRepository
import jp.speakbuddy.factsearcher.utils.JsonReader
import kotlin.random.Random

class TestFactRepository : FactRepository {
    override suspend fun getRandomFact(): FactNetworkModel {
        val list = JsonReader.getFactList()

        return list[getItemPosition(list.size-1)]
    }

    private fun getItemPosition(max: Int): Int {
        return Random.nextInt(from = 0, until = max)
    }
}