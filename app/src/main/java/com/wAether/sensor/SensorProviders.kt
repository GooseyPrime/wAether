package com.wAether.sensor

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.os.BatteryManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

private const val SENSOR_TAG = "SensorProviders"

/**
 * Provides device location using FusedLocationProviderClient.
 * Requires ACCESS_FINE_LOCATION or ACCESS_COARSE_LOCATION permission.
 */
class LocationProvider(private val context: Context) {
    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission") // Permissions should be checked before calling
    suspend fun getCurrentLocation(): Result<Location> {
        // Check for permissions (ideally this is done by the caller or a permission manager)
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.w(SENSOR_TAG, "Location permission not granted.")
            return Result.failure(SecurityException("Location permission not granted."))
        }

        return suspendCancellableCoroutine { continuation ->
            val cancellationTokenSource = CancellationTokenSource()
            fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY, // Or PRIORITY_BALANCED_POWER_ACCURACY
                cancellationTokenSource.token
            ).addOnSuccessListener { location: Location? ->
                if (location != null) {
                    Log.d(SENSOR_TAG, "Location fetched: $location")
                    if (continuation.isActive) continuation.resume(Result.success(location))
                } else {
                    Log.w(SENSOR_TAG, "FusedLocationProviderClient returned null location.")
                    if (continuation.isActive) continuation.resume(Result.failure(Exception("Failed to get location: null returned.")))
                }
            }.addOnFailureListener { exception ->
                Log.e(SENSOR_TAG, "Failed to get location", exception)
                if (continuation.isActive) continuation.resume(Result.failure(exception))
            }

            continuation.invokeOnCancellation {
                cancellationTokenSource.cancel()
            }
        }
    }
}

/**
 * Reads magnetic field strength from the device's magnetometer.
 * Requires BODY_SENSORS permission in some cases or specific sensor permissions.
 * The watch face spec lists BODY_SENSORS.
 */
class MagnetometerReader(private val context: Context) : SensorEventListener {
    private val sensorManager: SensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val magnetometer: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

    private var currentMagneticFieldStrength: Double? = null
    private var continuation: ((Result<Double?>) -> Unit)? = null

    suspend fun getMagneticFieldStrength(): Double? {
        if (magnetometer == null) {
            Log.w(SENSOR_TAG, "Magnetometer not available on this device.")
            return null
        }
        // This is a simplified approach for a one-time read.
        // For continuous updates, you'd register the listener and unregister it.
        // For a single value, we register, wait for one reading, then unregister.
        return suspendCancellableCoroutine { cont ->
            this.continuation = { result ->
                 if (cont.isActive) {
                    result.fold(
                        onSuccess = { cont.resume(it) },
                        onFailure = { cont.resumeWithException(it) }
                    )
                }
            }
            sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL)

            cont.invokeOnCancellation {
                sensorManager.unregisterListener(this)
                this.continuation = null // Clean up
            }
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_MAGNETIC_FIELD) {
            val magneticFieldX = event.values[0]
            val magneticFieldY = event.values[1]
            val magneticFieldZ = event.values[2]
            // Calculate the magnitude of the magnetic field vector
            currentMagneticFieldStrength = Math.sqrt(
                (magneticFieldX * magneticFieldX +
                        magneticFieldY * magneticFieldY +
                        magneticFieldZ * magneticFieldZ).toDouble()
            )
            Log.d(SENSOR_TAG, "Magnetic field strength: $currentMagneticFieldStrength µT")
            continuation?.invoke(Result.success(currentMagneticFieldStrength))
            // Unregister after the first reading for a one-shot request
            sensorManager.unregisterListener(this)
            this.continuation = null
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Can be used to handle changes in sensor accuracy if needed
        Log.d(SENSOR_TAG, "Magnetometer accuracy changed to: $accuracy")
    }
}

/**
 * Provides device-specific information.
 */
class DeviceInfoProvider(private val context: Context) {

    data class DeviceDetails(
        val deviceModel: String,
        val osVersion: String,
        val batteryLevel: Int?
    )

    fun getDeviceInfo(): DeviceDetails {
        val deviceModel = "${Build.MANUFACTURER} ${Build.MODEL}"
        val osVersion = "Android ${Build.VERSION.RELEASE} (SDK ${Build.VERSION.SDK_INT})"

        // Get battery level
        val batteryIntent: Intent? = context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        val level: Int = batteryIntent?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
        val scale: Int = batteryIntent?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
        val batteryPct: Int? = if (level != -1 && scale != -1) {
            (level.toFloat() / scale.toFloat() * 100.0f).toInt()
        } else {
            null
        }
        Log.d(SENSOR_TAG, "Device Model: $deviceModel, OS: $osVersion, Battery: $batteryPct%")
        return DeviceDetails(deviceModel, osVersion, batteryPct)
    }
}
