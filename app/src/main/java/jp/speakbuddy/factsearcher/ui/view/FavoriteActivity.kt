package jp.speakbuddy.factsearcher.ui.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import jp.speakbuddy.factsearcher.ui.data.FactUiModel
import jp.speakbuddy.factsearcher.ui.eventstate.FavoriteUiEvent
import jp.speakbuddy.factsearcher.ui.eventstate.FavoriteUiState
import jp.speakbuddy.factsearcher.ui.screen.FavoriteActivityScreen
import jp.speakbuddy.factsearcher.ui.theme.FactTheme
import jp.speakbuddy.factsearcher.ui.utils.LocaleManager
import jp.speakbuddy.factsearcher.ui.viewmodel.FavoriteActivityViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Locale

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
                FavoriteActivityScreen(
                    favoriteFactList,
                    onBack = { onBack() },
                    onDislikeFact = { dislikeFact(it) },
                    onLocaleSelected = { updateLocale(it) }
                )
            }
        }

        observe()
        fetchFavoriteItem()
    }

    override fun onResume() {
        super.onResume()
        LocaleManager.checkPageLocale(this)
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

    private fun onBack() {
        this.finish()
    }

    private fun updateLocale(locale: Locale){
        if (locale != this.resources.configuration.locales[0]) {
            val config = this.resources.configuration.apply {
                setLocale(locale)
            }

            this.resources.updateConfiguration(config, this.resources.displayMetrics)

            viewModel.onEvent(FavoriteUiEvent.UpdatePreferenceLanguage(locale))
            this.recreate()
        }
    }
}