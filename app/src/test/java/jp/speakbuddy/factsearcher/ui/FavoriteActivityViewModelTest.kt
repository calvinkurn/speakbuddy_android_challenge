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
import jp.speakbuddy.factsearcher.di.DispatchersProvider
import jp.speakbuddy.factsearcher.ui.data.FactUiModel
import jp.speakbuddy.factsearcher.domain.repository.DataStoreRepository
import jp.speakbuddy.factsearcher.domain.repository.UserPreferenceRepository
import jp.speakbuddy.factsearcher.domain.usecase.FavoriteUseCase
import jp.speakbuddy.factsearcher.domain.usecase.UserPreferencesUseCase
import jp.speakbuddy.factsearcher.ui.eventstate.FavoriteUiEvent
import jp.speakbuddy.factsearcher.ui.eventstate.FavoriteUiState
import jp.speakbuddy.factsearcher.ui.viewmodel.FavoriteActivityViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import java.util.Locale

class FavoriteActivityViewModelTest {
    private val dataStoreRepository: DataStoreRepository = mockk()
    private val dispatchersProvider: DispatchersProvider = mockk()
    private val userPreferenceRepository: UserPreferenceRepository = mockk()

    private lateinit var favoriteUseCase: FavoriteUseCase
    private lateinit var userPreferencesUseCase: UserPreferencesUseCase

    private lateinit var viewModel: FavoriteActivityViewModel

    @Before
    fun setup() {
        every { dispatchersProvider.io } returns Dispatchers.Unconfined
        every { dispatchersProvider.main } returns Dispatchers.Unconfined
        every { dispatchersProvider.default } returns Dispatchers.Unconfined

        favoriteUseCase = FavoriteUseCase(dataStoreRepository)
        userPreferencesUseCase = UserPreferencesUseCase(userPreferenceRepository)

        viewModel = FavoriteActivityViewModel(
            favoriteUseCase = favoriteUseCase,
            dispatchersProvider = dispatchersProvider,
            userPreferencesUseCase = userPreferencesUseCase
        )
    }

    @After
    fun cleanUp() {
        clearMocks(dataStoreRepository)
        clearMocks(dispatchersProvider)
    }

    @Test
    fun `Should get favorite fact list`() = runTest {
        coEvery { dataStoreRepository.getFavoriteList() } returns favoriteFactListSample

        viewModel.uiState.test {
            assertTrue(awaitItem() is FavoriteUiState.Initial)

            viewModel.onEvent(FavoriteUiEvent.GetFavoriteFact)

            val successItem = awaitItem()

            assertTrue(successItem is FavoriteUiState.Success)

            val favoriteListItem = (successItem as FavoriteUiState.Success).favoriteFactList
            assertTrue(favoriteListItem.size == favoriteFactListSample.size)
            assertTrue(favoriteListItem.first().fact == favoriteFactListSample.first().fact)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Should remove selected fact from favorite`() = runTest {
        val expectedResult = favoriteFactListSample.subList(1,2)

        coEvery { dataStoreRepository.removeFavoriteFact(any()) } just Runs
        coEvery { dataStoreRepository.getFavoriteList() } returns expectedResult

        viewModel.uiState.test {
            skipItems(1)

            viewModel.onEvent(FavoriteUiEvent.DislikeFact(favoriteFactListSample.first()))

            val successItem = awaitItem()

            assertTrue(successItem is FavoriteUiState.Success)

            val favoriteListItem = (successItem as FavoriteUiState.Success).favoriteFactList
            assertTrue(favoriteListItem.size == expectedResult.size)
            assertTrue(favoriteListItem.first().fact == expectedResult.first().fact)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Should update and get current preference language`() = runTest {
        val expectedLocale = Locale.JAPAN

        coEvery { userPreferenceRepository.setUserPreferenceLanguage(any()) } just Runs

        viewModel.uiState.test {
            skipItems(1)

            viewModel.onEvent(FavoriteUiEvent.UpdatePreferenceLanguage(expectedLocale))

            coVerify { userPreferenceRepository.setUserPreferenceLanguage(any()) }

            cancelAndIgnoreRemainingEvents()
        }
    }

    companion object {
        val favoriteFactListSample = listOf(
            FactUiModel(
                fact = "Sample fact 1",
                factId = 1L,
                isContainsCat = false,
                length = 13
            ),
            FactUiModel(
                fact = "Sample fact 2",
                factId = 2L,
                isContainsCat = false,
                length = 13
            )
        )
    }
}