package jp.speakbuddy.factsearcher.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jp.speakbuddy.factsearcher.R
import jp.speakbuddy.factsearcher.ui.data.FactUiModel
import jp.speakbuddy.factsearcher.ui.widget.FactWidget
import java.util.Locale

@Composable
fun FavoriteActivityScreen(
    favoriteFactList: List<FactUiModel>,
    onBack: () -> Unit,
    onDislikeFact: (fact: FactUiModel) -> Unit,
    onLocaleSelected: (locale: Locale) -> Unit
) {
    ScreenBase(
        pageTitle = stringResource(R.string.favorite_page_title),
        withBackNavigation = true,
        onNavigateBack = { onBack() },
        onLocaleUpdate = { onLocaleSelected(it) }
    ) { innerPadding ->
        Box(
            modifier = Modifier.padding(top = innerPadding.calculateTopPadding())
        ) {
            LazyColumn {
                items(favoriteFactList) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 10.dp)
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