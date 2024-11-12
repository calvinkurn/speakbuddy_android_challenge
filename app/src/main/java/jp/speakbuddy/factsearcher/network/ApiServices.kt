package jp.speakbuddy.factsearcher.network

import jp.speakbuddy.factsearcher.network.model.FactResponse
import retrofit2.http.GET

interface ApiServices {
    @GET("/fact")
    suspend fun getFact(): FactResponse
}