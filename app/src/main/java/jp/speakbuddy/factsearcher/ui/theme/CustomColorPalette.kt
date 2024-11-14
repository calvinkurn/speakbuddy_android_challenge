package jp.speakbuddy.factsearcher.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class CustomColorsPalette(
    val favoriteOutline: Color = Color.Unspecified,
    val favoriteFilled: Color = Color.Unspecified,
    val circularLoadingTrail: Color = Color.Unspecified,
    val circularLoading: Color = Color.Unspecified
)

val lightCustomColor get() = CustomColorsPalette(
    favoriteOutline = LightColorCollection.FavoriteOutlineLight.color,
    favoriteFilled = LightColorCollection.FavoriteFilledLight.color,
    circularLoadingTrail = LightColorCollection.CircularLoadingTrail.color,
    circularLoading = LightColorCollection.CircularLoading.color
)

val darkCustomColor get() = CustomColorsPalette(
    favoriteOutline = DarkColorCollection.FavoriteOutlineDark.color,
    favoriteFilled = DarkColorCollection.FavoriteFilledDark.color,
    circularLoadingTrail = DarkColorCollection.CircularLoadingTrail.color,
    circularLoading = DarkColorCollection.CircularLoading.color
)

val LocalCustomColorsPalette = staticCompositionLocalOf { CustomColorsPalette() }