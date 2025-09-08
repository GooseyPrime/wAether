package com.wAether.data.repository

import android.content.Context
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.wAether.data.model.GoesXrayFluxEntry
import com.wAether.data.model.LogEntry
import com.wAether.data.model.OpenMeteoResponse
import com.wAether.data.network.NoaaSwpcService
import com.wAether.data.network.OpenMeteoParameters
import com.wAether.data.network.OpenMeteoService
import com.wAether.data.network.RetrofitClient
import com.wAether.sensor.DeviceInfoProvider
import com.wAether.sensor.LocationProvider
import com.wAether.sensor.MagnetometerReader
import com.wAether.util.FlareCalculator
import com.wAether.util.WeatherCodeUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.TimeZone

/**
 * Repository for fetching all data required by the wAether app.
 * It abstracts data sources (network, sensors, device info) from the ViewModels
 * and handles saving data to Firebase.
 */
class DataRepository(
    private val context: Context,
    private val openMeteoService: OpenMeteoService = RetrofitClient.openMeteoService,
    private val noaaSwpcService: NoaaSwpcService = RetrofitClient.noaaSwpcService,
    private val locationProvider: LocationProvider = LocationProvider(context),
    private val magnetometerReader: MagnetometerReader = MagnetometerReader(context),
    private val deviceInfoProvider: DeviceInfoProvider = DeviceInfoProvider(context),
    private val firestore: FirebaseFirestore = Firebase.firestore // Firestore instance
) {
    private val TAG = "DataRepository"

    // Collection names in Firestore
    private val MOOD_LOGS_COLLECTION = "mood_logs"
    private val GLOBAL_SNAPSHOTS_COLLECTION = "global_snapshots"

    fun getCombinedLogData(): Flow<Result<LogEntryPartialData>> = flow {
        try {
            val locationResult = locationProvider.getCurrentLocation()
            if (locationResult.isFailure) {
                emit(Result.failure(locationResult.exceptionOrNull() ?: Exception("Failed to get location")))
                return@flow
            }
            val location = locationResult.getOrThrow()
            val lat = location.latitude
            val lon = location.longitude

            coroutineScope {
                val openMeteoDeferred = async { fetchOpenMeteoData(lat, lon) }
                val noaaKpDeferred = async { fetchLatestKpIndex() }
                val noaaXrayDeferred = async { fetchLatestXrayFlux() }
                val noaaSolarWindDeferred = async { fetchLatestSolarWind() }
                val magneticFieldDeferred = async { magnetometerReader.getMagneticFieldStrength() }
                val deviceInfoDeferred = async { deviceInfoProvider.getDeviceInfo() }

                val openMeteoResult = openMeteoDeferred.await()
                val kpIndexResult = noaaKpDeferred.await()
                val xrayFluxResult = noaaXrayDeferred.await()
                val solarWindResult = noaaSolarWindDeferred.await()
                val magneticFieldStrength = magneticFieldDeferred.await()
                val deviceInfo = deviceInfoDeferred.await()

                if (openMeteoResult.isFailure) {
                    emit(Result.failure(openMeteoResult.exceptionOrNull() ?: Exception("OpenMeteo API call failed")))
                    return@coroutineScope
                }

                val openMeteoData = openMeteoResult.getOrNull()
                val solarWindData = solarWindResult.getOrNull()

                val partialData = LogEntryPartialData(
                    latitude = lat,
                    longitude = lon,
                    timezoneOffset = openMeteoData?.current?.time?.let { getCurrentTimezoneOffset() } ?: getCurrentTimezoneOffset(),
                    magneticField = magneticFieldStrength,
                    kpIndex = kpIndexResult.getOrNull(),
                    irradiance = openMeteoData?.hourly?.shortwaveRadiation?.lastOrNull { it != null },
                    xrayFlux = xrayFluxResult.getOrNull()?.flux,
                    xrayClass = xrayFluxResult.getOrNull()?.flux?.let { FlareCalculator.getFlareClass(it) },
                    moonPhase = openMeteoData?.daily?.moonPhase?.firstOrNull { it != null },
                    solarWindSpeed = solarWindData?.speed,
                    solarWindDensity = solarWindData?.density,
                    deviceModel = deviceInfo.deviceModel,
                    osVersion = deviceInfo.osVersion,
                    batteryLevel = deviceInfo.batteryLevel,
                    localTemperature = openMeteoData?.current?.temperature2m,
                    localFeelsLikeTemperature = openMeteoData?.current?.apparentTemperature,
                    localHumidity = openMeteoData?.current?.relativeHumidity2m,
                    localWeatherCode = openMeteoData?.current?.weatherCode,
                    localWeatherCondition = openMeteoData?.current?.weatherCode?.let {
                        WeatherCodeUtil.getWeatherDescription(
                            it
                        )
                    },
                    localWindSpeed = openMeteoData?.current?.windSpeed10m,
                    localPrecipitation = openMeteoData?.current?.precipitation
                )
                emit(Result.success(partialData))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching combined log data", e)
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)

    // --- Firebase Operations ---

    /**
     * Saves a LogEntry to Firebase Firestore.
     * If mood is present, it goes to 'mood_logs'. Otherwise, 'global_snapshots'.
     */
    suspend fun saveLogEntryToFirebase(logEntry: LogEntry): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val collectionName = if (logEntry.mood != null) MOOD_LOGS_COLLECTION else GLOBAL_SNAPSHOTS_COLLECTION
            // Firestore automatically generates document IDs if you use .add()
            // Using .set() on a new doc() also works if you want to define your own ID,
            // but for logs, auto-ID is usually fine.
            firestore.collection(collectionName)
                .add(logEntry.toMap()) // Use the toMap() method from LogEntry
                .await() // Wait for the operation to complete
            Log.i(TAG, "LogEntry successfully saved to $collectionName: ${logEntry.timestamp}")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Error saving LogEntry to Firebase ($MOOD_LOGS_COLLECTION)", e)
            Result.failure(e)
        }
    }

    // --- Private helper functions for data fetching (as before) ---
    private suspend fun fetchOpenMeteoData(lat: Double, lon: Double): Result<OpenMeteoResponse> {
        return try {
            val response = openMeteoService.getForecastData(
                latitude = lat,
                longitude = lon,
                currentParams = OpenMeteoParameters.CURRENT_WEATHER_PARAMS,
                hourlyParams = OpenMeteoParameters.HOURLY_SOLAR_PARAMS,
                dailyParams = OpenMeteoParameters.DAILY_MOON_PARAMS,
                timezone = "auto"
            )
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Log.w(TAG, "OpenMeteo API error: ${response.code()} - ${response.message()}")
                Result.failure(Exception("OpenMeteo API error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "OpenMeteo API exception", e)
            Result.failure(e)
        }
    }

    private suspend fun fetchLatestKpIndex(): Result<Int?> {
        return try {
            val response = noaaSwpcService.getPlanetaryKpIndex()
            if (response.isSuccessful && response.body() != null) {
                val data = response.body()!!
                val latestKpString = data.getOrNull(1)?.getOrNull(1)
                val kpValue = latestKpString?.let { parseKpString(it) }
                Result.success(kpValue)
            } else {
                Log.w(TAG, "NOAA Kp Index API error: ${response.code()} - ${response.message()}")
                Result.failure(Exception("NOAA Kp Index API error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "NOAA Kp Index API exception", e)
            Result.failure(e)
        }
    }

    private fun parseKpString(kpString: String): Int? {
        return kpString.firstOrNull { it.isDigit() }?.digitToIntOrNull()
    }

    private suspend fun fetchLatestXrayFlux(): Result<GoesXrayFluxEntry?> {
        return try {
            val response = noaaSwpcService.getGoesXrayFlux()
            if (response.isSuccessful && response.body() != null) {
                val allFluxes = response.body()!!
                val latestRelevantFlux = allFluxes
                    .filter { it.energyBand == "0.1-0.8nm" }
                    .maxByOrNull { it.timeTag ?: "" }
                Result.success(latestRelevantFlux)
            } else {
                Log.w(TAG, "NOAA X-ray Flux API error: ${response.code()} - ${response.message()}")
                Result.failure(Exception("NOAA X-ray Flux API error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "NOAA X-ray Flux API exception", e)
            Result.failure(e)
        }
    }
    data class SolarWindDataPoint(val speed: Double?, val density: Double?)
    private suspend fun fetchLatestSolarWind(): Result<SolarWindDataPoint?> {
        return try {
            val response = noaaSwpcService.getRealTimeSolarWind()
            if (response.isSuccessful && response.body() != null) {
                val data = response.body()!!
                val latestDataRow = data.getOrNull(1)
                if (latestDataRow != null && latestDataRow.size >= 3) {
                    val density = latestDataRow.getOrNull(1)?.toDoubleOrNull()
                    val speed = latestDataRow.getOrNull(2)?.toDoubleOrNull()
                    if (density != null && speed != null) {
                        Result.success(SolarWindDataPoint(speed = speed, density = density))
                    } else {
                        Result.success(null)
                    }
                } else {
                    Result.success(null)
                }
            } else {
                Log.w(TAG, "NOAA Solar Wind API error: ${response.code()} - ${response.message()}")
                Result.failure(Exception("NOAA Solar Wind API error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "NOAA Solar Wind API exception", e)
            Result.failure(e)
        }
    }

    private fun getCurrentTimezoneOffset(): Int {
        val tz = TimeZone.getDefault()
        val now = System.currentTimeMillis()
        return tz.getOffset(now) / (1000 * 60 * 60)
    }
}

/**
 * Data class to hold the partially assembled data before creating the final LogEntry.
 * (Content remains the same as previous version)
 */
data class LogEntryPartialData(
    val latitude: Double?,
    val longitude: Double?,
    val timezoneOffset: Int?,
    val magneticField: Double?,
    val kpIndex: Int?,
    val irradiance: Double?,
    val xrayFlux: Double?,
    val xrayClass: String?,
    val moonPhase: Double?,
    val solarWindSpeed: Double?,
    val solarWindDensity: Double?,
    val deviceModel: String?,
    val osVersion: String?,
    val batteryLevel: Int?,
    val localTemperature: Double?,
    val localFeelsLikeTemperature: Double?,
    val localHumidity: Int?,
    val localWeatherCode: Int?,
    val localWeatherCondition: String?,
    val localWindSpeed: Double?,
    val localPrecipitation: Double?
) {
    fun toLogEntry(mood: String? = null, timestamp: Long = System.currentTimeMillis()): LogEntry {
        return LogEntry(
            timestamp = timestamp,
            timezoneOffset = this.timezoneOffset,
            latitude = this.latitude,
            longitude = this.longitude,
            magneticField = this.magneticField,
            kpIndex = this.kpIndex,
            irradiance = this.irradiance,
            xrayClass = this.xrayClass,
            moonPhase = this.moonPhase,
            solarWindSpeed = this.solarWindSpeed,
            solarWindDensity = this.solarWindDensity,
            deviceModel = this.deviceModel,
            osVersion = this.osVersion,
            batteryLevel = this.batteryLevel,
            localTemperature = this.localTemperature,
            localFeelsLikeTemperature = this.localFeelsLikeTemperature,
            localHumidity = this.localHumidity,
            localWeatherCondition = this.localWeatherCondition,
            localWeatherCode = this.localWeatherCode,
            localWindSpeed = this.localWindSpeed,
            localPrecipitation = this.localPrecipitation,
            mood = mood
        )
    }
}
