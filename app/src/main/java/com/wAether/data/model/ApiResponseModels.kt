package com.wAether.data.model

import com.google.gson.annotations.SerializedName

// --- Open-Meteo API Response Models ---

data class OpenMeteoResponse(
    val latitude: Double?,
    val longitude: Double?,
    @SerializedName("generationtime_ms")
    val generationTimeMs: Double?,
    @SerializedName("utc_offset_seconds")
    val utcOffsetSeconds: Int?,
    val timezone: String?,
    @SerializedName("timezone_abbreviation")
    val timezoneAbbreviation: String?,
    val elevation: Double?,

    // For current weather, solar irradiance, moon data etc.
    // We can request current, hourly, and daily data in one call.

    @SerializedName("current_units") // Renamed from current_weather_units for clarity
    val currentUnits: OpenMeteoCurrentUnits?,
    @SerializedName("current") // Renamed from current_weather for clarity
    val current: OpenMeteoCurrentData?,


    @SerializedName("hourly_units")
    val hourlyUnits: OpenMeteoHourlyUnits?,
    val hourly: OpenMeteoHourlyData?,

    @SerializedName("daily_units")
    val dailyUnits: OpenMeteoDailyUnits?,
    val daily: OpenMeteoDailyData?
)

// New: Models for Current Weather from Open-Meteo
data class OpenMeteoCurrentUnits(
    val time: String?, // e.g., "iso8601"
    @SerializedName("interval")
    val interval: String?, // e.g., "seconds"
    @SerializedName("temperature_2m")
    val temperature2m: String?, // e.g., "°C"
    @SerializedName("relative_humidity_2m")
    val relativeHumidity2m: String?, // e.g., "%"
    @SerializedName("apparent_temperature")
    val apparentTemperature: String?, // e.g., "°C"
    @SerializedName("is_day")
    val isDay: String?, // e.g., "" (usually unitless, 0 or 1)
    @SerializedName("precipitation")
    val precipitation: String?, // e.g., "mm"
    @SerializedName("rain")
    val rain: String?, // e.g., "mm"
    @SerializedName("showers")
    val showers: String?, // e.g., "mm"
    @SerializedName("snowfall")
    val snowfall: String?, // e.g., "cm"
    @SerializedName("weather_code")
    val weatherCode: String?, // e.g., "wmo code"
    @SerializedName("cloud_cover")
    val cloudCover: String?, // e.g., "%"
    @SerializedName("pressure_msl")
    val pressureMsl: String?, // e.g., "hPa"
    @SerializedName("surface_pressure")
    val surfacePressure: String?, // e.g., "hPa"
    @SerializedName("wind_speed_10m")
    val windSpeed10m: String?, // e.g., "km/h"
    @SerializedName("wind_direction_10m")
    val windDirection10m: String?, // e.g., "°"
    @SerializedName("wind_gusts_10m")
    val windGusts10m: String? // e.g., "km/h"
)

data class OpenMeteoCurrentData(
    val time: String?, // ISO8601 timestamp
    val interval: Int?, // e.g., 900 (seconds)
    @SerializedName("temperature_2m")
    val temperature2m: Double?,
    @SerializedName("relative_humidity_2m")
    val relativeHumidity2m: Int?,
    @SerializedName("apparent_temperature")
    val apparentTemperature: Double?,
    @SerializedName("is_day")
    val isDay: Int?, // 1 for day, 0 for night
    @SerializedName("precipitation")
    val precipitation: Double?,
    @SerializedName("rain")
    val rain: Double?,
    @SerializedName("showers")
    val showers: Double?,
    @SerializedName("snowfall")
    val snowfall: Double?,
    @SerializedName("weather_code")
    val weatherCode: Int?, // WMO code
    @SerializedName("cloud_cover")
    val cloudCover: Int?,
    @SerializedName("pressure_msl")
    val pressureMsl: Double?,
    @SerializedName("surface_pressure")
    val surfacePressure: Double?,
    @SerializedName("wind_speed_10m")
    val windSpeed10m: Double?,
    @SerializedName("wind_direction_10m")
    val windDirection10m: Int?,
    @SerializedName("wind_gusts_10m")
    val windGusts10m: Double?
)
// End New Current Weather Models


data class OpenMeteoHourlyUnits(
    val time: String?,
    @SerializedName("shortwave_radiation")
    val shortwaveRadiation: String?,
    @SerializedName("direct_normal_irradiance")
    val directNormalIrradiance: String?
)

data class OpenMeteoHourlyData(
    val time: List<String>?,
    @SerializedName("shortwave_radiation")
    val shortwaveRadiation: List<Double?>?,
    @SerializedName("direct_normal_irradiance")
    val directNormalIrradiance: List<Double?>?
)

data class OpenMeteoDailyUnits(
    val time: String?,
    val moonrise: String?,
    val moonset: String?,
    @SerializedName("moon_phase")
    val moonPhase: String?
)

data class OpenMeteoDailyData(
    val time: List<String>?,
    val moonrise: List<String?>?,
    val moonset: List<String?>?,
    @SerializedName("moon_phase")
    val moonPhase: List<Double?>?
)


// --- NOAA SWPC (Space Weather Prediction Center) API Response Models ---
// ... (No changes to NOAA models for this feature) ...
data class NoaaKpIndexEntry(
    @SerializedName(value = "time_tag", alternate = ["0"])
    val timeTag: String?,
    @SerializedName(value = "kp_index", alternate = ["1"])
    val kpIndexValue: String?,
    @SerializedName(value = "observed_time_tag", alternate = ["2"])
    val observationTimeTag: String?,
    @SerializedName(value = "data_type_code", alternate = ["3"])
    val dataTypeCode: String?
)

data class GoesXrayFluxEntry(
    @SerializedName("time_tag")
    val timeTag: String?,
    @SerializedName("flux")
    val flux: Double?,
    @SerializedName("energy")
    val energyBand: String?,
)

data class NoaaSolarWindEntry(
    @SerializedName(value = "time_tag", alternate = ["0"])
    val timeTag: String?,
    @SerializedName(value = "density", alternate = ["1"])
    val density: String?,
    @SerializedName(value = "speed", alternate = ["2"])
    val speed: String?,
    @SerializedName(value = "temperature", alternate = ["3"])
    val temperature: String?
)
