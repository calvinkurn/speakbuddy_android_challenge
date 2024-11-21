package jp.speakbuddy.factsearcher.ui.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import jp.speakbuddy.factsearcher.ui.data.DefaultFactUiModel
import jp.speakbuddy.factsearcher.ui.eventstate.FactUiEvent
import jp.speakbuddy.factsearcher.ui.eventstate.FactUiState
import jp.speakbuddy.factsearcher.ui.screen.FactActivityScreen
import jp.speakbuddy.factsearcher.ui.viewmodel.FactActivityViewModel
import jp.speakbuddy.factsearcher.ui.theme.FactTheme
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Locale

@AndroidEntryPoint
class FactActivity : ComponentActivity() {
    private val viewModel: FactActivityViewModel by viewModels()

    private var factIsLoading by mutableStateOf(false)
    private var factData by mutableStateOf(DefaultFactUiModel)
    private var isFactFavorite by mutableStateOf(false)
    private var errorMsg by mutableStateOf("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FactTheme(
                dynamicColor = false
            ) {
                FactActivityScreen(
                    factData = factData,
                    isFactFavorite = isFactFavorite,
                    isLoading = factIsLoading,
                    errorMsg = errorMsg,
                    onErrorDismiss = { errorMsg = "" },
                    onUpdateFactClicked = { if (!factIsLoading){ updateFact() } },
                    onNavigateToFavorite = { navigateToFavoritePage() },
                    onFavoriteClicked = { addFavoriteFact() },
                    onLocaleSelected = { updateLocale(it) }
                )
            }
        }

        observe()
        getLastFact()
    }

    override fun onResume() {
        super.onResume()
        viewModel.onEvent(FactUiEvent.CheckFavorite)
    }

    override fun onPause() {
        super.onPause()
        viewModel.onEvent(FactUiEvent.SaveFact)
    }

    private fun getLastFact() {
        viewModel.onEvent(FactUiEvent.RestoreSavedFact)
    }

    private fun updateFact() {
        viewModel.onEvent(FactUiEvent.GetFact)
    }

    private fun addFavoriteFact() {
        viewModel.onEvent(FactUiEvent.AddFactToFavorite)
    }

    private fun navigateToFavoritePage() {
        val intent = Intent(this, FavoriteActivity::class.java)
        startActivity(intent)
    }

    private fun updateLocale(locale: Locale){
        val config = this.resources.configuration.apply {
            setLocale(locale)
        }

        this.resources.updateConfiguration(config, this.resources.displayMetrics)
        this.recreate()
    }

    private fun observe() {
        lifecycleScope.launch {
            viewModel.uiState.collectLatest {
                factIsLoading = false
                when(it) {
                    is FactUiState.Loading -> {
                        factIsLoading = true
                    }
                    is FactUiState.Success -> {
                        factData = it.data
                        isFactFavorite = it.isFavorite
                    }
                    is FactUiState.FavoriteFact -> {
                        isFactFavorite = it.isFavorite
                    }
                    is FactUiState.Error -> {
                        errorMsg = it.errorMsg
                    }
                    else -> {}
                }
            }
        }
    }
}