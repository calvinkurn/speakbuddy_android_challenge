package jp.speakbuddy.factsearcher.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jp.speakbuddy.factsearcher.R
import jp.speakbuddy.factsearcher.ui.data.FactUiModel
import jp.speakbuddy.factsearcher.ui.widget.FactWidget
import jp.speakbuddy.factsearcher.ui.widget.HeaderWidget
import java.util.Locale

@Composable
fun FavoriteActivityScreen(
    favoriteFactList: List<FactUiModel>,
    onBack: () -> Unit,
    onDislikeFact: (fact: FactUiModel) -> Unit,
    onLocaleSelected: (locale: Locale) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column {
            HeaderWidget(
                title = stringResource(R.string.favorite_page_title),
                onBackClick = { onBack() },
                onLocaleSelected = { onLocaleSelected(it) }
            )

            LazyColumn {
                items(favoriteFactList) {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 10.dp)
                    ) {
                        FactWidget(
                            factData = it,
                            isLiked = true,
                            isAllowedScrollAble = false,
                            onFavoriteClick = {
                                onDislikeFact(it)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun FavoriteActivityScreenPreview() {
    val favoriteList = listOf(
        FactUiModel(
            fact = "Test Fact",
            isContainsCat = false,
            length = 9,
            factId = -1L
        )
    )

    FavoriteActivityScreen(
        favoriteFactList = favoriteList,
        onBack = {},
        onDislikeFact = {},
        onLocaleSelected = {}
    )
}