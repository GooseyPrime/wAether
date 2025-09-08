package com.wAether.ui.theme

import androidx.compose.runtime.Composable
import androidx.wear.compose.material.MaterialTheme

@Composable
fun WAetherTheme(
    // Dynamic theming (dark/light) is less common on Wear OS as it's primarily dark.
    // However, you could add a darkTheme: Boolean = isSystemInDarkTheme() parameter if needed.
    content: @Composable () -> Unit
) {
    // For Wear OS, we typically use a dark color palette.
    // The `wearColorPalette` is defined in Color.kt
    // The `wearTypography` is defined in Typography.kt

    MaterialTheme(
        colors = wearColorPalette,
        // typography = wearTypography, // Temporarily disabled due to compatibility issues
        // Shapes are less customized on Wear OS due to the circular screen,
        // but you can define them if needed (e.g., for cards).
        // shapes = Shapes, // You would define a Shapes.kt file similar to Color.kt and Typography.kt
        content = content
    )
}
