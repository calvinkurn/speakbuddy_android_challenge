package jp.speakbuddy.factsearcher.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jp.speakbuddy.factsearcher.R
import java.util.Locale

@Composable
fun LanguageWidget(
    currentLocale: Locale?,
    locales: List<Locale>,
    onLocaleSelected: (Locale) -> Unit
) {
    var isDropdownExpanded by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(8.dp)
    ) {

        val painterRes = if (currentLocale == Locale.JAPAN) {
            R.drawable.jp_flag_icon
        } else {
            R.drawable.en_flag_icon
        }

        Icon(
            painter = painterResource(painterRes),
            contentDescription = "Language Icon",
            modifier = Modifier
                .size(24.dp)
                .clickable { isDropdownExpanded = true },
            tint = Color.Unspecified
        )

        // Dropdown Menu for Locale Selection
        DropdownMenu(
            expanded = isDropdownExpanded,
            onDismissRequest = { isDropdownExpanded = false },
            modifier = Modifier
                .wrapContentSize()
                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
        ) {
            locales.forEach { locale ->
                DropdownMenuItem(
                    onClick = {
                        isDropdownExpanded = false
                        onLocaleSelected(locale)
                    },
                    text = {
                        Text(
                            text = locale.displayLanguage,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Start
                        )
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun LanguageWidgetPreview() {
    LanguageWidget(
        currentLocale = Locale.US,
        locales = listOf(Locale.JAPAN, Locale.US),
        onLocaleSelected = {}
    )
}