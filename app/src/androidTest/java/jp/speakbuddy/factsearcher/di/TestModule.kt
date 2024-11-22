package jp.speakbuddy.factsearcher.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import jp.speakbuddy.factsearcher.domain.repository.DataStoreRepository
import jp.speakbuddy.factsearcher.domain.repository.FactRepository
import jp.speakbuddy.factsearcher.domain.repository.UserPreferenceRepository
import jp.speakbuddy.factsearcher.network.ApiServices
import jp.speakbuddy.factsearcher.network.RetrofitProvider
import jp.speakbuddy.factsearcher.repository.TestDataStoreRepository
import jp.speakbuddy.factsearcher.repository.TestDispatchersProvider
import jp.speakbuddy.factsearcher.repository.TestFactRepository
import jp.speakbuddy.factsearcher.repository.TestUserPreferencesRepository
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AppModule::class]
)
object FakeFactRepositoryModule {
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
    fun provideFactRepository(): FactRepository {
        return TestFactRepository()
    }

    @Provides
    @Singleton
    fun provideFavoriteRepository(): DataStoreRepository {
        return TestDataStoreRepository()
    }

    @Provides
    @Singleton
    fun provideDispatcherProvider(): DispatchersProvider {
        return TestDispatchersProvider()
    }

    @Provides
    @Singleton
    fun provideUserPreferences(): UserPreferenceRepository {
        return TestUserPreferencesRepository()
    }
}