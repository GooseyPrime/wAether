package com.wAether.util

import org.junit.Assert.*
import org.junit.Test

class WeatherCodeUtilTest {

    @Test
    fun getWeatherDescription_clearSky_returnsCorrectDescription() {
        assertEquals("Clear sky", WeatherCodeUtil.getWeatherDescription(0))
    }

    @Test
    fun getWeatherDescription_mainlyClear_returnsCorrectDescription() {
        assertEquals("Mainly clear", WeatherCodeUtil.getWeatherDescription(1))
    }

    @Test
    fun getWeatherDescription_partlyCloudy_returnsCorrectDescription() {
        assertEquals("Partly cloudy", WeatherCodeUtil.getWeatherDescription(2))
    }

    @Test
    fun getWeatherDescription_overcast_returnsCorrectDescription() {
        assertEquals("Overcast", WeatherCodeUtil.getWeatherDescription(3))
    }

    @Test
    fun getWeatherDescription_fog_returnsCorrectDescription() {
        assertEquals("Fog", WeatherCodeUtil.getWeatherDescription(45))
        assertEquals("Depositing rime fog", WeatherCodeUtil.getWeatherDescription(48))
    }

    @Test
    fun getWeatherDescription_drizzle_returnsCorrectDescription() {
        assertEquals("Light drizzle", WeatherCodeUtil.getWeatherDescription(51))
        assertEquals("Moderate drizzle", WeatherCodeUtil.getWeatherDescription(53))
        assertEquals("Dense drizzle", WeatherCodeUtil.getWeatherDescription(55))
        assertEquals("Light freezing drizzle", WeatherCodeUtil.getWeatherDescription(56))
        assertEquals("Dense freezing drizzle", WeatherCodeUtil.getWeatherDescription(57))
    }

    @Test
    fun getWeatherDescription_rain_returnsCorrectDescription() {
        assertEquals("Slight rain", WeatherCodeUtil.getWeatherDescription(61))
        assertEquals("Moderate rain", WeatherCodeUtil.getWeatherDescription(63))
        assertEquals("Heavy rain", WeatherCodeUtil.getWeatherDescription(65))
        assertEquals("Light freezing rain", WeatherCodeUtil.getWeatherDescription(66))
        assertEquals("Heavy freezing rain", WeatherCodeUtil.getWeatherDescription(67))
    }

    @Test
    fun getWeatherDescription_snow_returnsCorrectDescription() {
        assertEquals("Slight snow fall", WeatherCodeUtil.getWeatherDescription(71))
        assertEquals("Moderate snow fall", WeatherCodeUtil.getWeatherDescription(73))
        assertEquals("Heavy snow fall", WeatherCodeUtil.getWeatherDescription(75))
        assertEquals("Snow grains", WeatherCodeUtil.getWeatherDescription(77))
    }

    @Test
    fun getWeatherDescription_showers_returnsCorrectDescription() {
        assertEquals("Slight rain showers", WeatherCodeUtil.getWeatherDescription(80))
        assertEquals("Moderate rain showers", WeatherCodeUtil.getWeatherDescription(81))
        assertEquals("Violent rain showers", WeatherCodeUtil.getWeatherDescription(82))
        assertEquals("Slight snow showers", WeatherCodeUtil.getWeatherDescription(85))
        assertEquals("Heavy snow showers", WeatherCodeUtil.getWeatherDescription(86))
    }

    @Test
    fun getWeatherDescription_thunderstorm_returnsCorrectDescription() {
        assertEquals("Thunderstorm: Slight or moderate", WeatherCodeUtil.getWeatherDescription(95))
        assertEquals("Thunderstorm with slight hail", WeatherCodeUtil.getWeatherDescription(96))
        assertEquals("Thunderstorm with heavy hail", WeatherCodeUtil.getWeatherDescription(99))
    }

    @Test
    fun getWeatherDescription_unknownCode_returnsUnknownCondition() {
        assertEquals("Unknown condition", WeatherCodeUtil.getWeatherDescription(999))
        assertEquals("Unknown condition", WeatherCodeUtil.getWeatherDescription(-1))
        assertEquals("Unknown condition", WeatherCodeUtil.getWeatherDescription(50))
    }

    @Test
    fun getWeatherDescription_nullCode_returnsUnknownCondition() {
        assertEquals("Unknown condition", WeatherCodeUtil.getWeatherDescription(null))
    }

    @Test
    fun getWeatherDescription_allValidCodes_returnsNonEmptyString() {
        val validCodes = listOf(
            0, 1, 2, 3, 45, 48, 51, 53, 55, 56, 57, 61, 63, 65, 66, 67,
            71, 73, 75, 77, 80, 81, 82, 85, 86, 95, 96, 99
        )
        
        validCodes.forEach { code ->
            val description = WeatherCodeUtil.getWeatherDescription(code)
            assertNotNull("Description should not be null for code $code", description)
            assertTrue("Description should not be empty for code $code", description.isNotEmpty())
            assertNotEquals("Description should not be 'Unknown condition' for valid code $code", 
                "Unknown condition", description)
        }
    }
}