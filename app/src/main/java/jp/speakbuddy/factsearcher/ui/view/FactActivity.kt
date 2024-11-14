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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import jp.speakbuddy.factsearcher.R
import jp.speakbuddy.factsearcher.ui.viewmodel.FactActivityViewModel
import jp.speakbuddy.factsearcher.ui.theme.FactTheme
import jp.speakbuddy.factsearcher.ui.theme.LocalCustomColorsPalette
import jp.speakbuddy.factsearcher.ui.widget.FactWidget
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FactActivity : ComponentActivity() {
    private val viewModel: FactActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val factIsLoading by viewModel.isLoading.collectAsState()

            FactTheme(
                dynamicColor = false
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column {
                        Column(
                            Modifier
                                .weight(1f)
                                .padding(16.dp),
                            verticalArrangement = Arrangement.Center
                        ) {
                            FactWidget(
                                factData = viewModel.factContent.collectAsState().value,
                                isLiked = viewModel.isFavorite.collectAsState().value,
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
            }
        }

        observe()
        getLastFact()
    }

    private fun getLastFact() {
        viewModel.restoreLastFact()
    }

    private fun updateFact() {
        viewModel.updateFact()
    }

    private fun addFavoriteFact() {
        viewModel.addFactToFavorite()
    }

    private fun navigateToFavoritePage() {
        val intent = Intent(this, FavoriteActivity::class.java)
        startActivity(intent)
    }

    private fun observe() {
        lifecycleScope.launch {
            viewModel.errorState.collectLatest {
                // TODO: Provide UI for error state
            }
        }
    }
}