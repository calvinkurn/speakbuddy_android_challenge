package jp.speakbuddy.factsearcher.ui.theme

import androidx.compose.ui.graphics.Color

// Light
enum class LightColorCollection(val color: Color) {
    Background(Color(0xFFFFFFFF)),

    Surface(Color(0xFFFBF0CD)),
    OnSurface(Color(0xFF3C3C3C)),

    FavoriteOutlineLight(Color(0xFF757575)),
    FavoriteFilledLight(Color(0xFFF9585E)),

    CircularLoadingTrail(Color(0x0DFFFFFF)),
    CircularLoading(Color(0xFFFFFFFF)),
}

// Dark
enum class DarkColorCollection(val color: Color) {
    Background(Color(0xFF121212)),

    Surface(Color(0xFF9C7C57 )),
    OnSurface(Color(0xFF2C2C2C)),

    FavoriteOutlineDark(Color(0xFFBDBDBD)),
    FavoriteFilledDark(Color(0xFFB33C3F)),

    CircularLoadingTrail(Color(0x0CFFFFFF)),
    CircularLoading(Color(0x50FFFFFF)),
}
