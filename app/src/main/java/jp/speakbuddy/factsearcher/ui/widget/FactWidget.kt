package jp.speakbuddy.factsearcher.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import jp.speakbuddy.factsearcher.R
import jp.speakbuddy.factsearcher.ui.theme.FactTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun FactScreen(
    fact: StateFlow<String>,
    isLoading: StateFlow<Boolean>,
    onUpdateClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            space = 16.dp,
            alignment = Alignment.CenterVertically
        )
    ) {
        val factContent by fact.collectAsState()
        val factIsLoading by isLoading.collectAsState()

        Text(
            text = stringResource(R.string.fact_widget_title),
            style = MaterialTheme.typography.titleLarge
        )

        Text(
            text = factContent,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )

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
private fun FactScreenPreview() {
    FactTheme {
        FactScreen(
            fact = MutableStateFlow(""),
            isLoading = MutableStateFlow(true)
        )
    }
}
