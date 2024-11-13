package jp.speakbuddy.factsearcher.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class CustomColorsPalette(
    val favoriteOutline: Color = Color.Unspecified,
    val favoriteFilled: Color = Color.Unspecified,
)

val lightCustomColor get() = CustomColorsPalette(
    favoriteOutline = FavoriteOutlineLight,
    favoriteFilled = FavoriteFilledLight
)

val darkCustomColor get() = CustomColorsPalette(
    favoriteOutline = FavoriteOutlineDark,
    favoriteFilled = FavoriteFilledDark
)

val LocalCustomColorsPalette = staticCompositionLocalOf { CustomColorsPalette() }