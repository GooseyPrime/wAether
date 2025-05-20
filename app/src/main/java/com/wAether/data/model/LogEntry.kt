package com.wAether.data.model

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.PropertyName

/**
 * Represents a single log entry for wAether, to be stored in Firebase Firestore.
 * This class is used for both mood_logs (with 'mood' field populated)
 * and global_snapshots (with 'mood' field null).
 */
data class LogEntry(
    val timestamp: Long = System.currentTimeMillis(),

    @get:PropertyName("timezoneOffset")
    @set:PropertyName("timezoneOffset")
    var timezoneOffset: Int? = null,

    val latitude: Double? = null,
    val longitude: Double? = null,

    // Geophysical Data
    @get:PropertyName("magneticField")
    @set:PropertyName("magneticField")
    var magneticField: Double? = null, // in µT

    @get:PropertyName("kpIndex")
    @set:PropertyName("kpIndex")
    var kpIndex: Int? = null,

    // Cosmic Data
    val irradiance: Double? = null, // Solar irradiance in W/m²

    @get:PropertyName("xrayClass")
    @set:PropertyName("xrayClass")
    var xrayClass: String? = null, // Solar X-ray flare classification

    // Device Info
    @get:PropertyName("deviceModel")
    @set:PropertyName("deviceModel")
    var deviceModel: String? = null,

    @get:PropertyName("osVersion")
    @set:PropertyName("osVersion")
    var osVersion: String? = null,

    @get:PropertyName("batteryLevel")
    @set:PropertyName("batteryLevel")
    var batteryLevel: Int? = null, // Percentage

    // Local Weather Data
    @get:PropertyName("localTemperature")
    @set:PropertyName("localTemperature")
    var localTemperature: Double? = null, // e.g., in Celsius

    @get:PropertyName("localFeelsLikeTemperature")
    @set:PropertyName("localFeelsLikeTemperature")
    var localFeelsLikeTemperature: Double? = null, // e.g., in Celsius

    @get:PropertyName("localHumidity")
    @set:PropertyName("localHumidity")
    var localHumidity: Int? = null, // Percentage

    @get:PropertyName("localWeatherCondition")
    @set:PropertyName("localWeatherCondition")
    var localWeatherCondition: String? = null, // Text description, e.g., "Clear sky"

    @get:PropertyName("localWeatherCode")
    @set:PropertyName("localWeatherCode")
    var localWeatherCode: Int? = null, // WMO Weather code

    @get:PropertyName("localWindSpeed")
    @set:PropertyName("localWindSpeed")
    var localWindSpeed: Double? = null, // e.g., in km/h or m/s

    @get:PropertyName("localPrecipitation")
    @set:PropertyName("localPrecipitation")
    var localPrecipitation: Double? = null, // e.g., in mm

    // Mood
    val mood: String? = null
) {
    // No-argument constructor for Firestore deserialization
    constructor() : this(
        timestamp = 0L,
        timezoneOffset = null,
        latitude = null,
        longitude = null,
        magneticField = null,
        kpIndex = null,
        irradiance = null,
        xrayClass = null,
        deviceModel = null,
        osVersion = null,
        batteryLevel = null,
        localTemperature = null,
        localFeelsLikeTemperature = null,
        localHumidity = null,
        localWeatherCondition = null,
        localWeatherCode = null,
        localWindSpeed = null,
        localPrecipitation = null,
        mood = null
    )

    fun toMap(): Map<String, Any?> {
        return mapOf(
            "timestamp" to timestamp,
            "timezoneOffset" to timezoneOffset,
            "latitude" to latitude,
            "longitude" to longitude,
            "magneticField" to magneticField,
            "kpIndex" to kpIndex,
            "irradiance" to irradiance,
            "xrayClass" to xrayClass,
            "deviceModel" to deviceModel,
            "osVersion" to osVersion,
            "batteryLevel" to batteryLevel,
            "localTemperature" to localTemperature,
            "localFeelsLikeTemperature" to localFeelsLikeTemperature,
            "localHumidity" to localHumidity,
            "localWeatherCondition" to localWeatherCondition,
            "localWeatherCode" to localWeatherCode,
            "localWindSpeed" to localWindSpeed,
            "localPrecipitation" to localPrecipitation,
            "mood" to mood
        ).filterValues { it != null }
    }
}
