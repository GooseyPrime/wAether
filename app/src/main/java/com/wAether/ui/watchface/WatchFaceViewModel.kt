package com.wAether.ui.watchface

import android.util.Log
import androidx.lifecycle.ViewModel // Using androidx.lifecycle.ViewModel for potential future ViewModel-scoped DI
import androidx.lifecycle.viewModelScope // Use viewModelScope if this were a standard Android ViewModel
import com.wAether.data.model.LogEntry
import com.wAether.data.repository.DataRepository
import com.wAether.data.repository.LogEntryPartialData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

private const val TAG = "WatchFaceViewModel"
private const val DATA_REFRESH_INTERVAL_MS = 30 * 60 * 1000L // 30 minutes, align with global_snapshots default

/**
 * ViewModel for the WAetherWatchFace.
 * Responsible for fetching data from DataRepository and exposing it as StateFlows
 * for the WatchFaceRenderer to observe.
 *
 * @param dataRepository The repository to fetch data from.
 * @param externalScope A CoroutineScope provided by the WatchFaceService, tied to its lifecycle.
 */
class WatchFaceViewModel(
    private val dataRepository: DataRepository,
    private val externalScope: CoroutineScope // Use the scope from WatchFaceService
) {

    // --- UI State for overall data loading ---
    sealed interface WatchDataUiState {
        data object Loading : WatchDataUiState
        data class Success(val data: LogEntryPartialData) : WatchDataUiState
        data class Error(val message: String) : WatchDataUiState
    }

    private val _uiState = MutableStateFlow<WatchDataUiState>(WatchDataUiState.Loading)
    val uiState: StateFlow<WatchDataUiState> = _uiState.asStateFlow()

    // --- Individual data points as StateFlows for easier consumption by the renderer ---
    // These will be updated when _uiState is Success.

    private val _currentTime = MutableStateFlow(System.currentTimeMillis())
    val currentTime: StateFlow<Long> = _currentTime.asStateFlow()

    // Local Weather
    private val _localTemperature = MutableStateFlow<Double?>(null)
    val localTemperature: StateFlow<Double?> = _localTemperature.asStateFlow()

    private val _localWeatherCondition = MutableStateFlow<String?>(null)
    val localWeatherCondition: StateFlow<String?> = _localWeatherCondition.asStateFlow()

    // Cosmic & Geophysical Data
    private val _solarIrradiance = MutableStateFlow<Double?>(null)
    val solarIrradiance: StateFlow<Double?> = _solarIrradiance.asStateFlow()

    private val _moonPhase = MutableStateFlow<Double?>(null)
    val moonPhase: StateFlow<Double?> = _moonPhase.asStateFlow()

    private val _magneticField = MutableStateFlow<Double?>(null)
    val magneticField: StateFlow<Double?> = _magneticField.asStateFlow()

    private val _kpIndex = MutableStateFlow<Int?>(null)
    val kpIndex: StateFlow<Int?> = _kpIndex.asStateFlow()

    private val _xrayClass = MutableStateFlow<String?>(null)
    val xrayClass: StateFlow<String?> = _xrayClass.asStateFlow()

    // TODO: Add StateFlows for other data points as needed for the UI
    // e.g., solarWindSpeed, solarWindDensity, batteryLevel, etc.

    private var dataFetchJob: Job? = null
    private var clockJob: Job? = null
    private var latestPartialData: LogEntryPartialData? = null


    init {
        startDataUpdates()
        startClockUpdates()
    }

    private fun startClockUpdates() {
        clockJob?.cancel()
        clockJob = externalScope.launch {
            while (true) {
                _currentTime.value = System.currentTimeMillis()
                delay(1000) // Update every second for the clock
            }
        }
    }

    fun startDataUpdates() {
        dataFetchJob?.cancel() // Cancel any existing job
        dataFetchJob = externalScope.launch {
            while (true) {
                fetchWatchData()
                // TODO: Implement dynamic refresh interval based on SnapshotScheduler logic (High Alert, Sleep Mode)
                // For now, using a fixed interval.
                delay(DATA_REFRESH_INTERVAL_MS)
            }
        }
    }

    fun fetchWatchData() {
        Log.d(TAG, "Fetching watch data...")
        _uiState.value = WatchDataUiState.Loading

        dataRepository.getCombinedLogData()
            .onEach { result ->
                result.fold(
                    onSuccess = { partialData ->
                        Log.d(TAG, "Successfully fetched partial data: $partialData")
                        latestPartialData = partialData // Store for mood logging
                        _uiState.value = WatchDataUiState.Success(partialData)
                        updateIndividualStateFlows(partialData)
                    },
                    onFailure = { exception ->
                        Log.e(TAG, "Error fetching watch data", exception)
                        _uiState.value = WatchDataUiState.Error(exception.message ?: "Unknown error")
                        // Optionally, clear individual flows or keep stale data
                    }
                )
            }
            .catch { exception ->
                Log.e(TAG, "Exception in getCombinedLogData flow", exception)
                _uiState.value = WatchDataUiState.Error(exception.message ?: "Flow collection error")
            }
            .launchIn(externalScope) // Collect the flow within the provided scope
    }

    private fun updateIndividualStateFlows(data: LogEntryPartialData) {
        _localTemperature.value = data.localTemperature
        _localWeatherCondition.value = data.localWeatherCondition
        _solarIrradiance.value = data.irradiance
        _moonPhase.value = data.moonPhase
        _magneticField.value = data.magneticField
        _kpIndex.value = data.kpIndex
        _xrayClass.value = data.xrayClass
        // Update other StateFlows here
    }

    /**
     * Called when a user logs a mood.
     * It takes the latest fetched environmental data, adds the mood,
     * and triggers saving to Firebase (via DataRepository).
     */
    fun logMood(mood: String) {
        externalScope.launch {
            val currentData = latestPartialData
            if (currentData == null) {
                Log.w(TAG, "Cannot log mood, no environmental data available.")
                // Optionally, trigger a fresh data fetch or show an error
                _uiState.value = WatchDataUiState.Error("Data unavailable to log mood. Try again soon.")
                fetchWatchData() // Attempt to refresh data
                return@launch
            }

            val logEntry = currentData.toLogEntry(mood = mood)
            Log.d(TAG, "Logging mood: $mood with data: $logEntry")

            // TODO: Implement saveLogEntryToFirebase in DataRepository
            // try {
            //     dataRepository.saveLogEntryToFirebase(logEntry)
            //     Log.i(TAG, "Mood log successful: $mood")
            //     // Optionally provide feedback to the UI
            // } catch (e: Exception) {
            //     Log.e(TAG, "Failed to log mood to Firebase", e)
            //     // Optionally provide error feedback to the UI
            // }
        }
    }

    /**
     * Call this when the ViewModel is no longer needed (e.g., WatchFaceService.onDestroy)
     * to clean up coroutines. The externalScope cancellation should handle this,
     * but explicit cancellation of jobs is good practice.
     */
    fun onCleared() {
        Log.d(TAG, "WatchFaceViewModel cleared.")
        dataFetchJob?.cancel()
        clockJob?.cancel()
        // The externalScope is managed by the WatchFaceService, so it shouldn't be cancelled here.
    }
}
