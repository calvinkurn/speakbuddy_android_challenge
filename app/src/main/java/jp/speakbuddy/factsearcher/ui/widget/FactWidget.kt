package jp.speakbuddy.factsearcher.ui.widget

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import jp.speakbuddy.factsearcher.R
import jp.speakbuddy.factsearcher.domain.model.CatFactUiModel
import jp.speakbuddy.factsearcher.ui.theme.FactTheme
import jp.speakbuddy.factsearcher.ui.theme.LocalCustomColorsPalette
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun FactWidget(
    factData: StateFlow<CatFactUiModel>,
    isLoading: StateFlow<Boolean>,
    isLoved: StateFlow<Boolean>,
    onUpdateClick: () -> Unit = {},
    onFavoriteClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            alignment = Alignment.CenterVertically,
            space = 16.dp
        )
    ) {
        val factContent by factData.collectAsState()
        val factIsLoading by isLoading.collectAsState()
        val factIsLoved by isLoved.collectAsState()

        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = stringResource(R.string.fact_widget_title),
                style = MaterialTheme.typography.titleLarge,
            )

            AnimatedContent(
                modifier = Modifier.align(Alignment.CenterEnd),
                targetState = factIsLoved,
                label = "Like Animation"
            ) {
                var color = LocalCustomColorsPalette.current.favoriteOutline
                val painter = if (it) {
                    color = LocalCustomColorsPalette.current.favoriteFilled
                    painterResource(R.drawable.heart_icon_filled)
                } else {
                    painterResource(R.drawable.heart_icon_outline)
                }

                Icon(
                    modifier = Modifier.size(32.dp).clickable {
                        onFavoriteClick()
                    },
                    painter = painter,
                    contentDescription = "Favorite Icon",
                    tint = color
                )
            }
        }

        Text(
            text = factContent.fact,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )

        if (factContent.length > 100 || factContent.isContainsCat) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(
                    alignment = Alignment.CenterVertically,
                    space = 4.dp
                )
            ) {
                HorizontalDivider()
                if (factContent.length > 100) {
                    Text(
                        text = "Content length: ${factContent.length} character",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )
                }

                if (factContent.isContainsCat) {
                    Text(
                        text = stringResource(R.string.fact_multiple_cat),
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )
                }
                HorizontalDivider()
            }
        }

        Button(onClick = onUpdateClick) {
            if (factIsLoading) {
                CircularProgressIndicator(
                    color = Color.White,
                    trackColor = Color(0x0DFFFFFF),
                    strokeWidth = 2.5.dp,
                    modifier = Modifier.size(16.dp)
                )
            } else {
                Text(
                    text = stringResource(R.string.fact_widget_button_update_label),
                    fontSize = TextUnit(16f, TextUnitType.Sp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun FactWidgetPreview() {
    FactTheme {
        FactWidget(
            factData = MutableStateFlow(
                CatFactUiModel(
                    fact = "Lorem Lorem Lorem Lorem Lorem Lorem Lorem Lorem Lorem",
                    length = 120,
                    isContainsCat = true
                )
            ),
            isLoading = MutableStateFlow(false),
            isLoved = MutableStateFlow(false)
        )
    }
}
