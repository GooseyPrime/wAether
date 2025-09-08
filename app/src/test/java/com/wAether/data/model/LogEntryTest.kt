package com.wAether.data.model

import org.junit.Assert.*
import org.junit.Test

class LogEntryTest {

    @Test
    fun logEntry_defaultConstructor_setsDefaultValues() {
        val logEntry = LogEntry()
        
        assertEquals(0L, logEntry.timestamp)
        assertNull(logEntry.timezoneOffset)
        assertNull(logEntry.latitude)
        assertNull(logEntry.longitude)
        assertNull(logEntry.magneticField)
        assertNull(logEntry.kpIndex)
        assertNull(logEntry.irradiance)
        assertNull(logEntry.xrayClass)
        assertNull(logEntry.deviceModel)
        assertNull(logEntry.osVersion)
        assertNull(logEntry.batteryLevel)
        assertNull(logEntry.localTemperature)
        assertNull(logEntry.localFeelsLikeTemperature)
        assertNull(logEntry.localHumidity)
        assertNull(logEntry.localWeatherCondition)
        assertNull(logEntry.localWeatherCode)
        assertNull(logEntry.localWindSpeed)
        assertNull(logEntry.localPrecipitation)
        assertNull(logEntry.mood)
    }

    @Test
    fun logEntry_primaryConstructor_setsTimestampAutomatically() {
        val beforeCreation = System.currentTimeMillis()
        val logEntry = LogEntry()
        val afterCreation = System.currentTimeMillis()
        
        // The default constructor sets timestamp to 0, but the primary constructor
        // would use System.currentTimeMillis()
        val logEntryWithValues = LogEntry(
            latitude = 40.7128,
            longitude = -74.0060,
            mood = "happy"
        )
        
        assertTrue("Timestamp should be between before and after creation time",
            logEntryWithValues.timestamp >= beforeCreation && logEntryWithValues.timestamp <= afterCreation)
    }

    @Test
    fun logEntry_withAllValues_setsCorrectly() {
        val timestamp = 1640995200000L // Jan 1, 2022
        val logEntry = LogEntry(
            timestamp = timestamp,
            timezoneOffset = -300, // UTC-5
            latitude = 40.7128,
            longitude = -74.0060,
            magneticField = 50.5,
            kpIndex = 3,
            irradiance = 1366.0,
            xrayClass = "C2.1",
            deviceModel = "Samsung Galaxy Watch",
            osVersion = "Android 12",
            batteryLevel = 85,
            localTemperature = 22.5,
            localFeelsLikeTemperature = 24.0,
            localHumidity = 65,
            localWeatherCondition = "Partly cloudy",
            localWeatherCode = 2,
            localWindSpeed = 10.5,
            localPrecipitation = 0.0,
            mood = "content"
        )
        
        assertEquals(timestamp, logEntry.timestamp)
        assertEquals(-300, logEntry.timezoneOffset)
        assertEquals(40.7128, logEntry.latitude!!, 0.0001)
        assertEquals(-74.0060, logEntry.longitude!!, 0.0001)
        assertEquals(50.5, logEntry.magneticField!!, 0.0001)
        assertEquals(3, logEntry.kpIndex)
        assertEquals(1366.0, logEntry.irradiance!!, 0.0001)
        assertEquals("C2.1", logEntry.xrayClass)
        assertEquals("Samsung Galaxy Watch", logEntry.deviceModel)
        assertEquals("Android 12", logEntry.osVersion)
        assertEquals(85, logEntry.batteryLevel)
        assertEquals(22.5, logEntry.localTemperature!!, 0.0001)
        assertEquals(24.0, logEntry.localFeelsLikeTemperature!!, 0.0001)
        assertEquals(65, logEntry.localHumidity)
        assertEquals("Partly cloudy", logEntry.localWeatherCondition)
        assertEquals(2, logEntry.localWeatherCode)
        assertEquals(10.5, logEntry.localWindSpeed!!, 0.0001)
        assertEquals(0.0, logEntry.localPrecipitation!!, 0.0001)
        assertEquals("content", logEntry.mood)
    }

    @Test
    fun toMap_withAllValues_includesAllNonNullFields() {
        val logEntry = LogEntry(
            timestamp = 1640995200000L,
            timezoneOffset = -300,
            latitude = 40.7128,
            longitude = -74.0060,
            magneticField = 50.5,
            kpIndex = 3,
            irradiance = 1366.0,
            xrayClass = "C2.1",
            deviceModel = "Samsung Galaxy Watch",
            osVersion = "Android 12",
            batteryLevel = 85,
            localTemperature = 22.5,
            localFeelsLikeTemperature = 24.0,
            localHumidity = 65,
            localWeatherCondition = "Partly cloudy",
            localWeatherCode = 2,
            localWindSpeed = 10.5,
            localPrecipitation = 0.0,
            mood = "content"
        )
        
        val map = logEntry.toMap()
        
        assertEquals(19, map.size) // All non-null fields
        assertEquals(1640995200000L, map["timestamp"])
        assertEquals(-300, map["timezoneOffset"])
        assertEquals(40.7128, map["latitude"])
        assertEquals(-74.0060, map["longitude"])
        assertEquals(50.5, map["magneticField"])
        assertEquals(3, map["kpIndex"])
        assertEquals(1366.0, map["irradiance"])
        assertEquals("C2.1", map["xrayClass"])
        assertEquals("Samsung Galaxy Watch", map["deviceModel"])
        assertEquals("Android 12", map["osVersion"])
        assertEquals(85, map["batteryLevel"])
        assertEquals(22.5, map["localTemperature"])
        assertEquals(24.0, map["localFeelsLikeTemperature"])
        assertEquals(65, map["localHumidity"])
        assertEquals("Partly cloudy", map["localWeatherCondition"])
        assertEquals(2, map["localWeatherCode"])
        assertEquals(10.5, map["localWindSpeed"])
        assertEquals(0.0, map["localPrecipitation"])
        assertEquals("content", map["mood"])
    }

    @Test
    fun toMap_withNullValues_excludesNullFields() {
        val logEntry = LogEntry(
            timestamp = 1640995200000L,
            latitude = 40.7128,
            mood = "happy"
            // All other fields are null
        )
        
        val map = logEntry.toMap()
        
        assertEquals(3, map.size) // Only timestamp, latitude, and mood
        assertEquals(1640995200000L, map["timestamp"])
        assertEquals(40.7128, map["latitude"])
        assertEquals("happy", map["mood"])
        
        // Ensure null fields are not included
        assertFalse(map.containsKey("timezoneOffset"))
        assertFalse(map.containsKey("longitude"))
        assertFalse(map.containsKey("magneticField"))
        assertFalse(map.containsKey("kpIndex"))
        assertFalse(map.containsKey("irradiance"))
        assertFalse(map.containsKey("xrayClass"))
        assertFalse(map.containsKey("deviceModel"))
        assertFalse(map.containsKey("osVersion"))
        assertFalse(map.containsKey("batteryLevel"))
        assertFalse(map.containsKey("localTemperature"))
        assertFalse(map.containsKey("localFeelsLikeTemperature"))
        assertFalse(map.containsKey("localHumidity"))
        assertFalse(map.containsKey("localWeatherCondition"))
        assertFalse(map.containsKey("localWeatherCode"))
        assertFalse(map.containsKey("localWindSpeed"))
        assertFalse(map.containsKey("localPrecipitation"))
    }

    @Test
    fun toMap_emptyLogEntry_includesOnlyTimestamp() {
        val logEntry = LogEntry()
        val map = logEntry.toMap()
        
        assertEquals(1, map.size)
        assertEquals(0L, map["timestamp"])
    }

    @Test
    fun logEntry_globalSnapshot_hasNullMood() {
        val globalSnapshot = LogEntry(
            timestamp = 1640995200000L,
            latitude = 40.7128,
            longitude = -74.0060,
            magneticField = 50.5,
            kpIndex = 3,
            mood = null // Global snapshots have null mood
        )
        
        assertNull("Global snapshots should have null mood", globalSnapshot.mood)
    }

    @Test
    fun logEntry_moodLog_hasNonNullMood() {
        val moodLog = LogEntry(
            timestamp = 1640995200000L,
            latitude = 40.7128,
            longitude = -74.0060,
            mood = "excited"
        )
        
        assertEquals("excited", moodLog.mood)
        assertNotNull("Mood logs should have non-null mood", moodLog.mood)
    }

    @Test
    fun logEntry_boundaryValues_handledCorrectly() {
        val logEntry = LogEntry(
            timestamp = Long.MAX_VALUE,
            timezoneOffset = Int.MAX_VALUE,
            latitude = 90.0, // Max latitude
            longitude = 180.0, // Max longitude
            magneticField = Double.MAX_VALUE,
            kpIndex = 9, // Max Kp index
            irradiance = Double.MAX_VALUE,
            batteryLevel = 100, // Max battery
            localHumidity = 100, // Max humidity
            localWeatherCode = 99,
            localTemperature = Double.MAX_VALUE,
            localFeelsLikeTemperature = Double.MAX_VALUE,
            localWindSpeed = Double.MAX_VALUE,
            localPrecipitation = Double.MAX_VALUE
        )
        
        assertNotNull(logEntry)
        assertEquals(Long.MAX_VALUE, logEntry.timestamp)
        assertEquals(Int.MAX_VALUE, logEntry.timezoneOffset)
        assertEquals(90.0, logEntry.latitude!!, 0.0001)
        assertEquals(180.0, logEntry.longitude!!, 0.0001)
    }
}