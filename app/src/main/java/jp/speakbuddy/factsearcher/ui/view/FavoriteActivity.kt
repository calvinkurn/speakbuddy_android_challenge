package jp.speakbuddy.factsearcher.ui.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import jp.speakbuddy.factsearcher.data.ui.FactUiModel
import jp.speakbuddy.factsearcher.ui.eventstate.FavoriteUiEvent
import jp.speakbuddy.factsearcher.ui.eventstate.FavoriteUiState
import jp.speakbuddy.factsearcher.ui.theme.FactTheme
import jp.speakbuddy.factsearcher.ui.viewmodel.FavoriteActivityViewModel
import jp.speakbuddy.factsearcher.ui.widget.FactWidget
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoriteActivity : ComponentActivity() {
    private val viewModel: FavoriteActivityViewModel by viewModels()

    private var favoriteFactList = mutableStateListOf<FactUiModel>()

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
                        Text("Header")

                        LazyColumn(
                            modifier = Modifier.weight(1f).padding(horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(favoriteFactList) {
                                FactWidget(
                                    factData = it,
                                    isLiked = true,
                                    onFavoriteClick = {
                                        dislikeFact(it)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        observe()
        fetchFavoriteItem()
    }

    private fun observe() {
        lifecycleScope.launch {
            viewModel.uiState.collectLatest {
                when(it){
                    is FavoriteUiState.Success -> {
                        favoriteFactList.clear()
                        favoriteFactList.addAll(it.favoriteFactList)
                    }
                    is FavoriteUiState.Initial -> {}
                }
            }
        }
    }

    private fun fetchFavoriteItem() {
        viewModel.onEvent(FavoriteUiEvent.GetFavoriteFact)
    }

    private fun dislikeFact(targetFact: FactUiModel) {
        viewModel.onEvent(FavoriteUiEvent.DislikeFact(targetFact))
    }
}