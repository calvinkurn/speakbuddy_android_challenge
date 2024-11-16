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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import jp.speakbuddy.factsearcher.R
import jp.speakbuddy.factsearcher.data.ui.DefaultFactUiModel
import jp.speakbuddy.factsearcher.ui.eventstate.FactUiEvent
import jp.speakbuddy.factsearcher.ui.eventstate.FactUiState
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
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column {
                        HeaderWidget(
                            withBackButton = false,
                            title = stringResource(R.string.fact_page_title)
                        )

                        Column(
                            Modifier
                                .weight(1f)
                                .padding(16.dp),
                            verticalArrangement = Arrangement.Center
                        ) {
                            FactWidget(
                                factData = factData,
                                isLiked = isFactFavorite,
                                onFavoriteClick = { addFavoriteFact() }
                            )
                        }

                        Row(
                            Modifier.fillMaxWidth().padding(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Button(
                                modifier = Modifier.weight(1f),
                                onClick = { navigateToFavoritePage() }
                            ) {
                                Text(stringResource(R.string.fact_page_button_favorite_label))
                            }


                            Button(
                                modifier = Modifier.weight(1f),
                                onClick = { updateFact() }
                            ) {
                                if (factIsLoading) {
                                    CircularProgressIndicator(
                                        color = LocalCustomColorsPalette.current.circularLoading,
                                        trackColor = LocalCustomColorsPalette.current.circularLoadingTrail,
                                        strokeWidth = 2.5.dp,
                                        modifier = Modifier.size(16.dp)
                                    )
                                } else {
                                    Text(
                                        text = stringResource(R.string.fact_page_button_update_label),
                                        fontSize = TextUnit(16f, TextUnitType.Sp)
                                    )
                                }
                            }
                        }
                    }
                }

                if (errorMsg.isNotEmpty()) {
                    ErrorPopup(errorMsg, onDismiss = {
                        errorMsg = ""
                    })
                }
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