package jp.speakbuddy.factsearcher.ui

import app.cash.turbine.test
import io.mockk.Runs
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import jp.speakbuddy.factsearcher.data.network.FactNetworkModel
import jp.speakbuddy.factsearcher.di.DispatchersProvider
import jp.speakbuddy.factsearcher.ui.data.FactUiModel
import jp.speakbuddy.factsearcher.domain.repository.DataStoreRepository
import jp.speakbuddy.factsearcher.domain.repository.FactRepository
import jp.speakbuddy.factsearcher.domain.repository.UserPreferenceRepository
import jp.speakbuddy.factsearcher.domain.usecase.FactUseCase
import jp.speakbuddy.factsearcher.domain.usecase.FavoriteUseCase
import jp.speakbuddy.factsearcher.domain.usecase.SaveDataUseCase
import jp.speakbuddy.factsearcher.domain.usecase.UserPreferencesUseCase
import jp.speakbuddy.factsearcher.ui.eventstate.FactUiEvent
import jp.speakbuddy.factsearcher.ui.eventstate.FactUiState
import jp.speakbuddy.factsearcher.ui.eventstate.FavoriteUiEvent
import jp.speakbuddy.factsearcher.ui.viewmodel.FactActivityViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.util.Locale

class FactActivityViewModelTest {

    private val factRepository: FactRepository = mockk()
    private val dataStoreRepository: DataStoreRepository = mockk()
    private val dispatchersProvider: DispatchersProvider = mockk()
    private val userPreferenceRepository: UserPreferenceRepository = mockk()

    private lateinit var factUseCase: FactUseCase
    private lateinit var favoriteUseCase: FavoriteUseCase
    private lateinit var saveDataUseCase: SaveDataUseCase
    private lateinit var userPreferencesUseCase: UserPreferencesUseCase

    private lateinit var viewModel: FactActivityViewModel

    @Before
    fun setup() {
        every { dispatchersProvider.io } returns Dispatchers.Unconfined
        every { dispatchersProvider.main } returns Dispatchers.Unconfined
        every { dispatchersProvider.default } returns Dispatchers.Unconfined
        coEvery { userPreferenceRepository.getUserPreferenceLanguage() } returns Locale.US

        factUseCase = FactUseCase(factRepository)
        favoriteUseCase = FavoriteUseCase(dataStoreRepository)
        saveDataUseCase = SaveDataUseCase(dataStoreRepository)
        userPreferencesUseCase = UserPreferencesUseCase(userPreferenceRepository)

        viewModel = FactActivityViewModel(
            factUseCase, favoriteUseCase, saveDataUseCase, dispatchersProvider, userPreferencesUseCase
        )
    }

    @After
    fun cleanUp() {
        clearMocks(factRepository)
        clearMocks(dataStoreRepository)
        clearMocks(dispatchersProvider)
        clearMocks(userPreferenceRepository)
    }

    @Test
    fun `Should get new fact`() = runTest {
        val expectedFavoriteState = true

        coEvery { factRepository.getRandomFact() } returns factNetworkModelSample
        coEvery { dataStoreRepository.isFactFavorite(any()) } returns expectedFavoriteState

        viewModel.uiState.test {
            assertEquals(FactUiState.Initial, awaitItem())

            viewModel.onEvent(FactUiEvent.GetFact)

            val successResult = awaitItem()

            assertTrue(successResult is FactUiState.Success)

            val successData = successResult as FactUiState.Success
            assertEquals(factNetworkModelSample.fact, successData.data.fact)
            assertTrue(!successData.data.isContainsCat)

            viewModel.onEvent(FactUiEvent.CheckFavorite)

            val favoriteState = awaitItem() as FactUiState.FavoriteFact

            assertTrue(favoriteState.isFavorite)

            cancelAndIgnoreRemainingEvents()
        }
    }

    // Test FactUiEvent.RestoreSavedFact
    @Test
    fun `Should get saved last fact`() = runTest {
        val expectedFavoriteState = true

        coEvery { dataStoreRepository.isFactFavorite(any()) } returns expectedFavoriteState
        coEvery { dataStoreRepository.getSavedLatestFact() } returns factUiModelSample

        viewModel.uiState.test {
            viewModel.onEvent(FactUiEvent.RestoreSavedFact)

            skipItems(1)
            val savedData = awaitItem() as FactUiState.Success

            assertEquals(savedData.isFavorite, expectedFavoriteState)
            assertEquals(factUiModelSample.fact, savedData.data.fact)

            cancelAndIgnoreRemainingEvents()
        }
    }

    // Test FactUiEvent.AddFactToFavorite
    @Test
    fun `Should add fact to favorite list`() = runTest {
        coEvery { factRepository.getRandomFact() } returns factNetworkModelSample
        coEvery { dataStoreRepository.addFavoriteFact(any()) } just Runs
        coEvery { dataStoreRepository.saveLatestFact(any()) } just Runs

        viewModel.uiState.test {
            viewModel.onEvent(FactUiEvent.GetFact)
            viewModel.onEvent(FactUiEvent.AddFactToFavorite)

            skipItems(2)
            val newFavoriteState = awaitItem() as FactUiState.FavoriteFact

            assertTrue(newFavoriteState.isFavorite)

            cancelAndIgnoreRemainingEvents()
        }
    }

    // Test FactUiEvent.SaveFact
    @Test
    fun `Should add current fact to local storage`() = runTest {
        coEvery { factRepository.getRandomFact() } returns factNetworkModelSample
        coEvery { dataStoreRepository.addFavoriteFact(any()) } just Runs
        coEvery { dataStoreRepository.saveLatestFact(any()) } just Runs

        viewModel.uiState.test {
            viewModel.onEvent(FactUiEvent.GetFact)
            viewModel.onEvent(FactUiEvent.SaveFact)

            skipItems(2)
            coVerify { dataStoreRepository.saveLatestFact(any()) }
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Should update and get current preference language`() = runTest {
        val expectedLocale = Locale.JAPAN

        coEvery { userPreferenceRepository.setUserPreferenceLanguage(any()) } just Runs
        coEvery { userPreferenceRepository.getUserPreferenceLanguage() } returns expectedLocale

        viewModel.getLanguagePreference()
        verify { userPreferenceRepository.getUserPreferenceLanguage() }

        viewModel.uiState.test {
            skipItems(1)

            viewModel.onEvent(FactUiEvent.UpdatePreferenceLanguage(expectedLocale))

            coVerify { userPreferenceRepository.setUserPreferenceLanguage(any()) }

            cancelAndIgnoreRemainingEvents()
        }
    }

    companion object {
        val factUiModelSample = FactUiModel(
            factId = 123L,
            fact = "Fact Sample",
            length = 11,
            isContainsCat = false
        )

        val factNetworkModelSample = FactNetworkModel(
            fact = "Sample Fact",
            length = 11,
        )
    }
}
