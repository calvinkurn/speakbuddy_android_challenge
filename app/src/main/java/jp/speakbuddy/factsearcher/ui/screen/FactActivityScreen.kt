package jp.speakbuddy.factsearcher.ui.screen

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import jp.speakbuddy.factsearcher.R
import jp.speakbuddy.factsearcher.ui.data.DefaultFactUiModel
import jp.speakbuddy.factsearcher.ui.data.FactUiModel
import jp.speakbuddy.factsearcher.ui.theme.LocalCustomColorsPalette
import jp.speakbuddy.factsearcher.ui.widget.ErrorPopup
import jp.speakbuddy.factsearcher.ui.widget.FactWidget
import jp.speakbuddy.factsearcher.ui.widget.HeaderWidget
import java.util.Locale

const val FACT_SCREEN_FAVORITE_BUTTON_TAG = "FACT_SCREEN_FAVORITE_BUTTON_TAG"
const val FACT_SCREEN_UPDATE_FACT_BUTTON_TAG = "FACT_SCREEN_UPDATE_FACT_BUTTON_TAG"

@Composable
fun FactActivityScreen(
    factData: FactUiModel,
    isFactFavorite: Boolean,
    isLoading: Boolean,
    errorMsg: String,
    onErrorDismiss: () -> Unit,
    onFavoriteClicked: () -> Unit,
    onNavigateToFavorite: () -> Unit,
    onUpdateFactClicked: () -> Unit,
    onLocaleSelected: (Locale) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column {
            HeaderWidget(
                withBackButton = false,
                title = stringResource(R.string.fact_page_title),
                onLocaleSelected = {
                    onLocaleSelected(it)
                }
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
                    onFavoriteClick = { onFavoriteClicked() }
                )
            }

            Row(
                Modifier.fillMaxWidth().padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    modifier = Modifier.weight(1f).testTag(FACT_SCREEN_FAVORITE_BUTTON_TAG),
                    onClick = { onNavigateToFavorite() }
                ) {
                    Text(stringResource(R.string.fact_page_button_favorite_label))
                }


                Button(
                    modifier = Modifier.weight(1f).testTag(FACT_SCREEN_UPDATE_FACT_BUTTON_TAG),
                    onClick = { onUpdateFactClicked() }
                ) {
                    if (isLoading) {
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
            onErrorDismiss()
        })
    }
}

@Preview
@Composable
private fun FactActivityScreenPreview() {
    val factIsLoading = false
    val factData = DefaultFactUiModel
    val isFactFavorite = false
    val errorMsg = ""

    FactActivityScreen(
        factData = factData,
        isFactFavorite = isFactFavorite,
        isLoading = factIsLoading,
        errorMsg = errorMsg,
        onErrorDismiss = {},
        onUpdateFactClicked = {},
        onNavigateToFavorite = {},
        onFavoriteClicked = {},
        onLocaleSelected = {}
    )
}