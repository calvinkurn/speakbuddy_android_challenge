package jp.speakbuddy.factsearcher.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

class RetrofitProvider {
    companion object {
        private const val BASE_URL = "https://catfact.ninja"

        fun getInstance(): ApiServices {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
                .build()
                .create(ApiServices::class.java)
        }
    }
}
