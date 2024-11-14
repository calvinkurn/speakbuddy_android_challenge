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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dagger.hilt.android.AndroidEntryPoint
import jp.speakbuddy.factsearcher.data.ui.FactUiModel
import jp.speakbuddy.factsearcher.ui.theme.FactTheme
import jp.speakbuddy.factsearcher.ui.viewmodel.FavoriteActivityViewModel
import jp.speakbuddy.factsearcher.ui.widget.FactWidget

@AndroidEntryPoint
class FavoriteActivity : ComponentActivity() {
    private val viewModel: FavoriteActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val favoriteItem by viewModel.favoriteFactData.collectAsState()

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
                            items(favoriteItem) {
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

        fetchFavoriteItem()
    }

    private fun fetchFavoriteItem() {
        viewModel.getFavoriteFactData()
    }

    private fun dislikeFact(targetFact: FactUiModel) {
        viewModel.dislikeFact(targetFact)
    }
}