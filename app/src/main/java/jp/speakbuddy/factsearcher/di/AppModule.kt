package jp.speakbuddy.factsearcher.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jp.speakbuddy.factsearcher.domain.repository.FactRepository
import jp.speakbuddy.factsearcher.domain.repository.FactRepositoryImpl
import jp.speakbuddy.factsearcher.domain.repository.DataStoreRepository
import jp.speakbuddy.factsearcher.domain.repository.DataStoreRepositoryImpl
import jp.speakbuddy.factsearcher.domain.repository.UserPreferenceRepository
import jp.speakbuddy.factsearcher.domain.repository.UserPreferenceRepositoryImpl
import jp.speakbuddy.factsearcher.network.ApiServices
import jp.speakbuddy.factsearcher.network.RetrofitProvider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideApplicationContext(@ApplicationContext context: Context): Context = context

    @Provides
    @Singleton
    fun provideRetrofitInstance(): ApiServices {
        return RetrofitProvider.getInstance()
    }

    @Provides
    @Singleton
    fun provideFactRepository(apiServices: ApiServices): FactRepository {
        return FactRepositoryImpl(apiServices)
    }

    @Provides
    @Singleton
    fun provideFavoriteRepository(@ApplicationContext context: Context): DataStoreRepository {
        return DataStoreRepositoryImpl(context)
    }

    @Provides
    @Singleton
    fun provideDispatcherProvider(): DispatchersProvider {
        return DefaultDispatcherProvider()
    }

    @Provides
    @Singleton
    fun provideUserPreferenceRepository(@ApplicationContext context: Context): UserPreferenceRepository {
        return UserPreferenceRepositoryImpl(context)
    }
}