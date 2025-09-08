package com.wAether.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.wear.compose.material.Colors

// Define your app's primary, secondary, and other theme colors here.
// These are example colors; you should customize them for wAether's branding.

// Light Theme Colors (Example)
val PrimaryLight = Color(0xFF006971) // A deep teal/blue
val PrimaryVariantLight = Color(0xFF004A50) // Darker shade of primary
val SecondaryLight = Color(0xFF4A6365) // A muted teal
val SecondaryVariantLight = Color(0xFF00393E) // Darker shade of secondary
val BackgroundLight = Color(0xFFF7F9F9) // Off-white
val SurfaceLight = Color(0xFFF7F9F9) // Typically same as background for Wear OS
val ErrorLight = Color(0xFFB00020)
val OnPrimaryLight = Color.White
val OnSecondaryLight = Color.White
val OnBackgroundLight = Color.Black
val OnSurfaceLight = Color.Black // Text/icons on surface
val OnSurfaceVariantLight = Color(0xFF70797A) // For less prominent text/icons on surface
val OnErrorLight = Color.White

// Dark Theme Colors (Wear OS primarily uses a dark theme)
// For Wear OS, the default Material theme is already dark.
// You might want to define specific shades if the defaults aren't suitable.
val PrimaryDark = Color(0xFF50D7E2) // A brighter teal for dark theme
val PrimaryVariantDark = Color(0xFF80DEEA) // Lighter shade
val SecondaryDark = Color(0xFFB2CCCC) // A light, muted teal/cyan
val SecondaryVariantDark = Color(0xFF80CBC4)
val BackgroundDark = Color(0xFF1F1F1F) // Dark gray, typical for Wear OS
val SurfaceDark = Color(0xFF2C2C2C) // Slightly lighter dark gray for surfaces like cards
val ErrorDark = Color(0xFFCF6679) // Standard Material dark error color
val OnPrimaryDark = Color.Black
val OnSecondaryDark = Color.Black
val OnBackgroundDark = Color.White
val OnSurfaceDark = Color.White // Text/icons on dark surface
val OnSurfaceVariantDark = Color(0xFFB8C8C9) // For less prominent text/icons on dark surface
val OnErrorDark = Color.Black

// Wear OS specific color palette using the Colors data class from androidx.wear.compose.material
// Wear OS apps are typically dark themed.
val wearColorPalette: Colors = Colors(
    primary = PrimaryDark, // A bright, energetic blue, good for accents
    primaryVariant = PrimaryVariantDark, // A slightly lighter blue
    secondary = SecondaryDark, // A contrasting color, e.g., a warm orange or vibrant green
    secondaryVariant = SecondaryVariantDark,
    background = BackgroundDark, // Standard dark background for Wear OS
    surface = SurfaceDark, // For cards, buttons, etc.
    error = ErrorDark, // Standard error color
    onPrimary = OnPrimaryDark, // Text/icons on primary color
    onSecondary = OnSecondaryDark, // Text/icons on secondary color
    onBackground = OnBackgroundDark, // Text/icons on background
    onSurface = OnSurfaceDark, // Text/icons on surface
    onSurfaceVariant = OnSurfaceVariantDark // For less prominent text/icons
    // You can also define specific colors for Wear OS components if needed
)
