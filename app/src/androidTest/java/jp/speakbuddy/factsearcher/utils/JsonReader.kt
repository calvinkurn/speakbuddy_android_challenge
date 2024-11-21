package jp.speakbuddy.factsearcher.utils

import androidx.test.platform.app.InstrumentationRegistry
import jp.speakbuddy.factsearcher.data.network.FactNetworkModel
import kotlinx.serialization.json.Json
import java.io.InputStreamReader

private fun readJsonFile(fileName: String): String {
    val context = InstrumentationRegistry.getInstrumentation().context
    val inputStream = context.assets.open(fileName)
    return InputStreamReader(inputStream).use { it.readText() }
}

object JsonReader {
    private var catFactList: List<FactNetworkModel> = listOf()

    fun getFactList(): List<FactNetworkModel> {
        if (catFactList.isEmpty()) {
            readJsonFile("cat_fact.json").let {
                catFactList = Json.decodeFromString<CatFactResponse>(it).data
            }
        }

        return catFactList
    }
}