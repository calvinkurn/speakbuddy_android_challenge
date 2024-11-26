package jp.speakbuddy.factsearcher.ui.widget

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jp.speakbuddy.factsearcher.R
import java.util.Locale

const val HEADER_BACK_NAVIGATION_TAG = "HEADER_BACK_NAVIGATION_TAG"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeaderWidget(
    title: String,
    withBackButton: Boolean = true,
    onBackClick: () -> Unit = {},
    onLocaleSelected: (locale: Locale) -> Unit = {}
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
        },
        actions = {
            LanguageWidget(
                currentLocale = LocalContext.current.resources.configuration.locales[0],
                locales = listOf(Locale.US, Locale.JAPAN),
                onLocaleSelected = {
                    onLocaleSelected(it)
                }
            )
        },
        navigationIcon = {
            if (withBackButton) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .size(height = 24.dp, width = 32.dp)
                        .padding(end = 8.dp)
                        .testTag(HEADER_BACK_NAVIGATION_TAG)
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_icon_content_description),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    )
}

@Preview
@Composable
private fun HeaderWidgetPreview() {
    HeaderWidget(
        title = "Sample Title",
    )
}