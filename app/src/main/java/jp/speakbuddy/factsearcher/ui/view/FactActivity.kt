package jp.speakbuddy.factsearcher.ui.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import jp.speakbuddy.factsearcher.ui.viewmodel.FactActivityViewModel
import jp.speakbuddy.factsearcher.ui.theme.FactTheme
import jp.speakbuddy.factsearcher.ui.widget.FactWidget
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FactActivity : ComponentActivity() {
    private val viewModel: FactActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FactTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    FactWidget(
                        factData = viewModel.factContent,
                        isLoading = viewModel.isLoading,
                        isLoved = viewModel.isLoved,
                        onUpdateClick = { updateFact() },
                        onFavoriteClick = { addFavoriteFact() }
                    )
                }
            }
        }

        observe()
    }

    private fun updateFact() {
        viewModel.updateFact()
    }

    private fun addFavoriteFact(){
        viewModel.addFactToFavorite()
    }

    private fun observe() {
        lifecycleScope.launch {
            viewModel.errorState.collectLatest {
                // TODO: Provide UI for error state
            }
        }
    }
}