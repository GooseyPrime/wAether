package com.wAether.data.network

import com.wAether.data.model.OpenMeteoResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit service interface for Open-Meteo API.
 * Base URL: https://api.open-meteo.com/
 */
interface OpenMeteoService {

    @GET("v1/forecast")
    suspend fun getForecastData(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("current") currentParams: String, // New: For current weather
        @Query("hourly") hourlyParams: String,
        @Query("daily") dailyParams: String,
        @Query("temperature_unit") temperatureUnit: String = "celsius", // Or "fahrenheit"
        @Query("wind_speed_unit") windSpeedUnit: String = "kmh", // Or "ms", "mph", "kn"
        @Query("precipitation_unit") precipitationUnit: String = "mm", // Or "inch"
        @Query("timezone") timezone: String = "auto",
        @Query("forecast_days") forecastDays: Int = 1
    ): Response<OpenMeteoResponse>
}

/**
 * Helper object for API parameter strings
 */
object OpenMeteoParameters {
    // Request all potentially useful current weather parameters
    const val CURRENT_WEATHER_PARAMS = "temperature_2m,relative_humidity_2m,apparent_temperature,is_day,precipitation,rain,showers,snowfall,weather_code,cloud_cover,pressure_msl,surface_pressure,wind_speed_10m,wind_direction_10m,wind_gusts_10m"
    const val HOURLY_SOLAR_PARAMS = "shortwave_radiation,direct_normal_irradiance" // As before
    const val DAILY_MOON_PARAMS = "moonrise,moonset,moon_phase" // As before
}
