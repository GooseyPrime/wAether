package com.wAether.util

/**
 * Utility to interpret WMO Weather interpretation codes (WW codes) from Open-Meteo.
 * Reference: https://open-meteo.com/en/docs (Weather WMO Codes section)
 *
 * This provides a human-readable description and can be extended for icons.
 */
object WeatherCodeUtil {

    fun getWeatherDescription(code: Int?): String {
        return when (code) {
            0 -> "Clear sky"
            1 -> "Mainly clear"
            2 -> "Partly cloudy"
            3 -> "Overcast"
            45 -> "Fog"
            48 -> "Depositing rime fog"
            51 -> "Light drizzle"
            53 -> "Moderate drizzle"
            55 -> "Dense drizzle"
            56 -> "Light freezing drizzle"
            57 -> "Dense freezing drizzle"
            61 -> "Slight rain"
            63 -> "Moderate rain"
            65 -> "Heavy rain"
            66 -> "Light freezing rain"
            67 -> "Heavy freezing rain"
            71 -> "Slight snow fall"
            73 -> "Moderate snow fall"
            75 -> "Heavy snow fall"
            77 -> "Snow grains"
            80 -> "Slight rain showers"
            81 -> "Moderate rain showers"
            82 -> "Violent rain showers"
            85 -> "Slight snow showers"
            86 -> "Heavy snow showers"
            95 -> "Thunderstorm: Slight or moderate" // For simplicity, grouping 95, 96, 99
            96 -> "Thunderstorm with slight hail"
            99 -> "Thunderstorm with heavy hail"
            else -> "Unknown condition"
        }
    }

    // TODO: Add a function to map weather codes to drawable resource IDs for icons
    // fun getWeatherIcon(code: Int?, isDay: Boolean): Int { ... }
}
