package jp.speakbuddy.factsearcher.network

import jp.speakbuddy.factsearcher.data.network.FactNetworkModel
import retrofit2.http.GET

interface ApiServices {
    @GET("/fact")
    suspend fun getFact(): FactNetworkModel
}