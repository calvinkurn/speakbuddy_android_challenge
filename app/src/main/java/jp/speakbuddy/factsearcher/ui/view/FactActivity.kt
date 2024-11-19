package jp.speakbuddy.factsearcher.ui.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import jp.speakbuddy.factsearcher.R
import jp.speakbuddy.factsearcher.ui.data.DefaultFactUiModel
import jp.speakbuddy.factsearcher.ui.data.FactUiModel
import jp.speakbuddy.factsearcher.ui.eventstate.FactUiEvent
import jp.speakbuddy.factsearcher.ui.eventstate.FactUiState
import jp.speakbuddy.factsearcher.ui.screen.FactActivityScreen
import jp.speakbuddy.factsearcher.ui.viewmodel.FactActivityViewModel
import jp.speakbuddy.factsearcher.ui.theme.FactTheme
import jp.speakbuddy.factsearcher.ui.theme.LocalCustomColorsPalette
import jp.speakbuddy.factsearcher.ui.widget.ErrorPopup
import jp.speakbuddy.factsearcher.ui.widget.FactWidget
import jp.speakbuddy.factsearcher.ui.widget.HeaderWidget
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

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
                    onFavoriteClicked = { addFavoriteFact() }
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