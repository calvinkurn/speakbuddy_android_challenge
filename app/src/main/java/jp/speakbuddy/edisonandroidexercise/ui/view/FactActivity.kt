package jp.speakbuddy.edisonandroidexercise.ui.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import jp.speakbuddy.edisonandroidexercise.ui.widget.FactScreen
import jp.speakbuddy.edisonandroidexercise.ui.viewmodel.FactActivityViewModel
import jp.speakbuddy.edisonandroidexercise.ui.theme.CatFactTheme
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class FactActivity : ComponentActivity() {
    private val viewModel = FactActivityViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CatFactTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    FactScreen(
                        fact = viewModel.factContent,
                        isLoading = viewModel.isLoading,
                        onUpdateClick = { updateFact() }
                    )
                }
            }
        }

        observe()
    }

    private fun updateFact() {
        viewModel.updateFact()
    }

    private fun observe() {
        lifecycleScope.launch {
            viewModel.errorState.collectLatest {
                // TODO: Provide UI for error state
            }
        }
    }
}