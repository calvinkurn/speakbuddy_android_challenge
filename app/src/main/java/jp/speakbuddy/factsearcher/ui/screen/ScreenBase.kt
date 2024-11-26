package jp.speakbuddy.factsearcher.ui.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import jp.speakbuddy.factsearcher.ui.widget.HeaderWidget
import java.util.Locale

@Composable
fun ScreenBase(
    withBackNavigation: Boolean = true,
    pageTitle: String,
    onLocaleUpdate: (Locale) -> Unit = {},
    onNavigateBack: () -> Unit = {},
    content: @Composable (innerPadding: PaddingValues) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            HeaderWidget(
                title = pageTitle,
                withBackButton = withBackNavigation,
                onBackClick = onNavigateBack,
                onLocaleSelected = onLocaleUpdate
            )
        }
    ) { innerPadding ->
        content(innerPadding)
    }
}

@Preview
@Composable
private fun ScreenBasePreview() {
    ScreenBase(
        pageTitle = "Test Title",
    ) { 
        Text("Test")
    }
}