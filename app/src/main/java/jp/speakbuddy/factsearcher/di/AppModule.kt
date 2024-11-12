package jp.speakbuddy.factsearcher.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jp.speakbuddy.factsearcher.domain.repository.FactRepository
import jp.speakbuddy.factsearcher.domain.repository.FactRepositoryImpl
import jp.speakbuddy.factsearcher.network.ApiServices
import jp.speakbuddy.factsearcher.network.RetrofitProvider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideRetrofitInstance(): ApiServices {
        return RetrofitProvider.getInstance()
    }

    @Provides
    @Singleton
    fun provideFactRepository(apiServices: ApiServices): FactRepository{
        return FactRepositoryImpl(apiServices)
    }
}