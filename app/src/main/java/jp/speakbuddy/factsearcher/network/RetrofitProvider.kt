package jp.speakbuddy.factsearcher.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import jp.speakbuddy.factsearcher.BuildConfig
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

class RetrofitProvider {
    companion object {
        fun getInstance(): ApiServices {
            return Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
                .build()
                .create(ApiServices::class.java)
        }
    }
}
