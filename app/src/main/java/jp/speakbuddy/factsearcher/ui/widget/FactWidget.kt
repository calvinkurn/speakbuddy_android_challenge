package jp.speakbuddy.factsearcher.ui.widget

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jp.speakbuddy.factsearcher.R
import jp.speakbuddy.factsearcher.ui.data.FactUiModel
import jp.speakbuddy.factsearcher.ui.theme.FactTheme
import jp.speakbuddy.factsearcher.ui.theme.LocalCustomColorsPalette

const val FACT_WIDGET_TAG = "FACT_WIDGET_TEST_TAG"
const val FACT_WIDGET_CONTENT_TAG = "FACT_WIDGET_CONTENT_TEST_TAG"
const val FACT_WIDGET_LIKE_BUTTON = "FACT_WIDGET_LIKE_BUTTON"

@Composable
fun FactWidget(
    factData: FactUiModel,
    isLiked: Boolean,
    isAllowedScrollAble: Boolean = true,
    onFavoriteClick: () -> Unit = {}
) {
    val scrollState = rememberScrollState()

    ElevatedCard(
        modifier = Modifier.testTag(FACT_WIDGET_TAG),
        colors = CardDefaults.cardColors().copy(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(8.dp).let {
                if (isAllowedScrollAble) it.verticalScroll(scrollState) else it
            },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(
                alignment = Alignment.CenterVertically,
                space = 16.dp
            )
        ) {
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
                    targetState = isLiked,
                    label = stringResource(R.string.like_animation_content_description)
                ) {
                    // use tint color for dark mode adjustment color
                    var color = LocalCustomColorsPalette.current.favoriteOutline
                    val painter = if (it) {
                        color = LocalCustomColorsPalette.current.favoriteFilled
                        painterResource(R.drawable.heart_icon_filled)
                    } else {
                        painterResource(R.drawable.heart_icon_outline)
                    }

                    Icon(
                        modifier = Modifier
                            .size(32.dp)
                            .clickable {
                                onFavoriteClick()
                            }
                            .testTag(FACT_WIDGET_LIKE_BUTTON),
                        painter = painter,
                        contentDescription = stringResource(R.string.favorite_icon_content_description),
                        tint = color
                    )
                }
            }

            Text(
                modifier = Modifier.testTag(FACT_WIDGET_CONTENT_TAG),
                text = factData.fact,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )

            if (factData.length > 100 || factData.isContainsCat) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(
                        alignment = Alignment.CenterVertically,
                        space = 4.dp
                    )
                ) {
                    HorizontalDivider(
                        modifier = Modifier.padding(bottom = 4.dp),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (factData.length > 100) {
                        Text(
                            text = String.format(stringResource(R.string.fact_content_length), factData.length),
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center
                        )
                    }

                    if (factData.isContainsCat) {
                        Text(
                            text = stringResource(R.string.fact_multiple_cat),
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun FactWidgetPreview() {
    FactTheme {
        FactWidget(
            factData = FactUiModel(
                fact = "Lorem Lorem Lorem Lorem Lorem Lorem Lorem Lorem Lorem",
                length = 120,
                isContainsCat = true,
                factId = 0
            ),
            isLiked = false
        )
    }
}
