package com.wAether.ui.watchface

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.text.TextPaint
import android.view.SurfaceHolder
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.wear.watchface.ComplicationSlotsManager
import androidx.wear.watchface.CompositingRenderer
import androidx.wear.watchface.TapEvent
import androidx.wear.watchface.WatchFace
import androidx.wear.watchface.WatchState
import androidx.wear.watchface.style.CurrentUserStyleRepository
import com.wAether.ui.theme.WAetherTheme // Assuming you have a Compose theme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val TAG = "WatchFaceRenderer"

// Constants for drawing - adjust as needed
private const val AMBIENT_REFRESH_INTERVAL_MS = 60000L // 1 minute for ambient mode

class WatchFaceRenderer(
    private val context: Context,
    surfaceHolder: SurfaceHolder,
    watchState: WatchState,
    private val currentUserStyleRepository: CurrentUserStyleRepository,
    canvasType: Int,
    complicationSlotsManager: ComplicationSlotsManager, // Keep for future complication use
    private val watchFaceViewModel: WatchFaceViewModel,
    private val scope: CoroutineScope // Passed from WatchFaceService
) : CompositingRenderer(
    surfaceHolder,
    watchState,
    canvasType,
    complicationSlotsManager,
    interactiveDrawModeUpdateDelayMillis = 16L // Aim for 60fps in interactive
),
    WatchFace.TapListener {

    // --- Paint objects for Canvas drawing (if mixing with Compose Canvas) ---
    // It's generally preferred to do as much as possible with Compose drawing primitives.
    // These are examples if you need fine-grained control with Android's Paint.
    private val textPaint = TextPaint().apply {
        isAntiAlias = true
        textSize = 48f
        color = Color.WHITE
        textAlign = Paint.Align.CENTER
        typeface = Typeface.create("sans-serif-condensed", Typeface.BOLD)
    }
    private val ambientTextPaint = TextPaint().apply {
        isAntiAlias = true
        textSize = 48f
        color = Color.LTGRAY // Typically less bright for ambient
        textAlign = Paint.Align.CENTER
        typeface = Typeface.create("sans-serif-condensed", Typeface.NORMAL)
    }

    // --- State for rendering, collected from ViewModel ---
    // These will be used by the @Composable RootApp function
    private lateinit var currentTimeState: State<Long>
    private lateinit var uiState: State<WatchFaceViewModel.WatchDataUiState>
    private lateinit var localTemperatureState: State<Double?>
    private lateinit var localWeatherConditionState: State<String?>
    private lateinit var solarIrradianceState: State<Double?>
    private lateinit var moonPhaseState: State<Double?>
    private lateinit var magneticFieldState: State<Double?>
    private lateinit var kpIndexState: State<Int?>
    private lateinit var xrayClassState: State<String?>

    // Add other states as needed, e.g., for battery, solar wind

    init {
        // Initial data fetch when renderer is created
        scope.launch {
            watchFaceViewModel.fetchWatchData()
        }
    }

    override fun Mux.createRootComposables(currentUserStyleRepository: CurrentUserStyleRepository) {
        // Initialize State<T> delegates here, inside the Mux scope
        // This ensures they are recomposed correctly when ViewModel's StateFlows change.
        currentTimeState = watchFaceViewModel.currentTime.collectAsStateWithLifecycle(scope = scope)
        uiState = watchFaceViewModel.uiState.collectAsStateWithLifecycle(scope = scope)
        localTemperatureState = watchFaceViewModel.localTemperature.collectAsStateWithLifecycle(scope = scope)
        localWeatherConditionState = watchFaceViewModel.localWeatherCondition.collectAsStateWithLifecycle(scope = scope)
        solarIrradianceState = watchFaceViewModel.solarIrradiance.collectAsStateWithLifecycle(scope = scope)
        moonPhaseState = watchFaceViewModel.moonPhase.collectAsStateWithLifecycle(scope = scope)
        magneticFieldState = watchFaceViewModel.magneticField.collectAsStateWithLifecycle(scope = scope)
        kpIndexState = watchFaceViewModel.kpIndex.collectAsStateWithLifecycle(scope = scope)
        xrayClassState = watchFaceViewModel.xrayClass.collectAsStateWithLifecycle(scope = scope)

        // Add collections for other states

        addRootComposable("wAetherRoot") {
            WAetherTheme { // Apply your app's Compose theme
                // Pass the collected states to your main Composable
                RootApp(
                    currentTime = currentTimeState.value,
                    uiState = uiState.value,
                    localTemperature = localTemperatureState.value,
                    localWeatherCondition = localWeatherConditionState.value,
                    solarIrradiance = solarIrradianceState.value,
                    moonPhase = moonPhaseState.value,
                    magneticField = magneticFieldState.value,
                    kpIndex = kpIndexState.value,
                    xrayClass = xrayClassState.value,
                    isAmbient = watchState.isAmbient.collectAsStateWithLifecycle(scope = scope).value
                )
            }
        }
    }

    // --- Tap Handling ---
    override fun onTapEvent(tapType: Int, tapEvent: TapEvent, kompliksetData: Map<Int, WatchFace.KomplikData>) {
        Log.d(TAG, "Tap event: type=$tapType, x=${tapEvent.xPos}, y=${tapEvent.yPos}")
        // Handle tap events here.
        // Example: If a certain area is tapped, trigger mood logging or open MoodSelectorScreen.
        // For simplicity, let's assume a tap anywhere might initiate mood logging for now.
        // You'll need to define specific tap targets later.

        // This is a placeholder. You might want to check tapEvent.xPos and tapEvent.yPos
        // to see if the tap landed on a specific "mood log" button/area.
        // Or, if tapping anywhere on the face opens the mood selector:
        // watchFaceViewModel.logMood("TappedMoodPlaceholder") // Replace with actual mood selection flow

        // If MoodSelectorScreen is a separate activity:
        // val intent = Intent(context, MoodSelectorActivity::class.java) // Create MoodSelectorActivity
        // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        // context.startActivity(intent)

        // For now, let's log a placeholder. The actual mood selection UI needs to be integrated.
        // This could involve showing a Composable dialog on the watch face itself.
        Log.i(TAG, "Watch face tapped. Implement mood selection UI or navigation.")
        // Example: Triggering a mood log directly (for testing)
        // watchFaceViewModel.logMood("Happy")
    }

    // Optional: Override renderParameters to customize rendering based on watch state
    // override fun renderParameters(bounds: Rect, currentUserStyleRepository: CurrentUserStyleRepository, watchState: WatchState): RenderParameters {
    //     return super.renderParameters(bounds, currentUserStyleRepository, watchState)
    // }

    // Optional: Override userStyleFlavors for predefined style combinations
    // override val userStyleFlavors: List<UserStyleFlavor> get() = ...
}

// --- Main Composable for the Watch Face UI ---
@Composable
fun RootApp(
    currentTime: Long,
    uiState: WatchFaceViewModel.WatchDataUiState,
    localTemperature: Double?,
    localWeatherCondition: String?,
    solarIrradiance: Double?,
    moonPhase: Double?,
    magneticField: Double?,
    kpIndex: Int?,
    xrayClass: String?,
    isAmbient: Boolean
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        // Background (can be dynamic based on mood or conditions)
        // Canvas(modifier = Modifier.fillMaxSize()) { drawRect(color = androidx.compose.ui.graphics.Color.Black) }

        when (uiState) {
            is WatchFaceViewModel.WatchDataUiState.Loading -> {
                LoadingIndicator()
            }
            is WatchFaceViewModel.WatchDataUiState.Error -> {
                ErrorDisplay(message = uiState.message, currentTime = currentTime)
            }
            is WatchFaceViewModel.WatchDataUiState.Success -> {
                // Main display when data is available
                InteractiveDisplay(
                    currentTime = currentTime,
                    data = uiState.data, // This is LogEntryPartialData
                    localTemperature = localTemperature,
                    localWeatherCondition = localWeatherCondition,
                    solarIrradiance = solarIrradiance,
                    moonPhase = moonPhase,
                    magneticField = magneticField,
                    kpIndex = kpIndex,
                    xrayClass = xrayClass,
                    isAmbient = isAmbient
                )
            }
        }
    }
}

@Composable
fun InteractiveDisplay(
    currentTime: Long,
    data: LogEntryPartialData, // Contains all fetched data
    localTemperature: Double?,
    localWeatherCondition: String?,
    solarIrradiance: Double?,
    moonPhase: Double?,
    magneticField: Double?,
    kpIndex: Int?,
    xrayClass: String?,
    isAmbient: Boolean
) {
    // TODO: Implement the detailed visual layout as per your "Visual Display Plan"
    // This will involve creating Composables for each element:
    // - TimeDisplay
    // - MoodDisplay (tappable, emoji, color ring)
    // - MagneticFieldDisplay (pulse ring)
    // - IrradianceDisplay (sun icon, glow)
    // - MoonDisplay (phase icon, animated)
    // - KpIndexDisplay (ring meter)
    // - XrayFlareDisplay (symbol, flash)
    // - LocalWeatherDisplay

    // For now, a simple placeholder showing the time and some data
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        TimeDisplay(currentTime = currentTime, isAmbient = isAmbient)
        // Spacer(modifier = Modifier.height(8.dp))

        if (!isAmbient) { // Show more details in interactive mode
            Text(
                text = "Temp: ${localTemperature?.toInt() ?: "--"}°C, ${localWeatherCondition ?: "N/A"}",
                fontSize = 14.sp
            )
            Text(text = "Kp: ${kpIndex ?: "--"}, µT: ${magneticField?.toInt() ?: "--"}", fontSize = 14.sp)
            Text(
                text = "Irrad: ${solarIrradiance?.toInt() ?: "--"} W/m², Moon: ${moonPhase?.let { "%.2f".format(it) } ?: "--"}",
                fontSize = 14.sp
            )
            Text(text = "X-Ray: ${xrayClass ?: "None"}", fontSize = 14.sp)

            // Placeholder for Mood - this needs to be an interactive element
            // MoodDisplay(currentMood = data.mood, onMoodSelect = { /* trigger mood selection */ })
        }
    }
}

@Composable
fun TimeDisplay(currentTime: Long, isAmbient: Boolean) {
    val timeFormat = remember { SimpleDateFormat(if (isAmbient) "HH:mm" else "HH:mm:ss", Locale.getDefault()) }
    Text(
        text = timeFormat.format(Date(currentTime)),
        fontSize = if (isAmbient) 48.sp else 56.sp, // Slightly smaller in ambient
        color = if (isAmbient) androidx.compose.ui.graphics.Color.LightGray else androidx.compose.ui.graphics.Color.White,
        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
    )
}

@Composable
fun LoadingIndicator() {
    Text("wAether Loading...", fontSize = 20.sp)
    // You can use androidx.wear.compose.material.CircularProgressIndicator here
}

@Composable
fun ErrorDisplay(message: String, currentTime: Long) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        TimeDisplay(currentTime = currentTime, isAmbient = false) // Show time even on error
        Text("Error:", fontSize = 16.sp, color = androidx.compose.ui.graphics.Color.Red)
        Text(message, fontSize = 14.sp, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
    }
}

// --- TODO: Implement individual Composable functions for each data point ---
// Based on your "Visual Display Plan"

// @Composable
// fun MoodDisplay(currentMood: String?, isAmbient: Boolean, onMoodTap: () -> Unit) { ... }

// @Composable
// fun MagneticFieldDisplay(strength: Double?, isAmbient: Boolean) { ... use Canvas for pulse ring ... }

// @Composable
// fun IrradianceDisplay(irradiance: Double?, isAmbient: Boolean) { ... sun icon + glow ... }

// @Composable
// fun MoonDisplay(phase: Double?, isAmbient: Boolean) { ... phase icon, animated ... }

// @Composable
// fun KpIndexDisplay(kpIndex: Int?, isAmbient: Boolean) { ... ring meter ... }

// @Composable
// fun XrayFlareDisplay(xrayClass: String?, isAmbient: Boolean) { ... flash symbol ... }

// @Composable
// fun LocalWeatherDisplay(temperature: Double?, condition: String?, isAmbient: Boolean) { ... }

// Helper for collecting StateFlows with lifecycle awareness in Composables
// This is a common pattern if you don't have lifecycle-runtime-compose dependency
// or want a more explicit scope.
// However, with androidx.lifecycle:lifecycle-runtime-compose, just .collectAsState() is fine.
// For CompositingRenderer, we need to be careful about the scope.
// The Mux.createRootComposables provides the right context.
// A simpler approach is to use `StateFlow.value` directly if updates are frequent enough
// or rely on the recomposition triggered by `collectAsState` within the Mux block.

// Using .collectAsState() directly within the Mux.createRootComposables block
// and passing the resulting State<T> to your RootApp is the standard way.
// The example above has been updated to reflect this.
// For custom collection logic, you might use LaunchedEffect within your Composables.
// The `collectAsStateWithLifecycle` is a helper from lifecycle-runtime-compose, ensure you have the dependency.
// If not, a simple .collectAsState() within a scope that is managed (like the one provided by Mux) works.
// For simplicity, let's assume a helper or direct use of collectAsState.

// A more robust way to collect states within CompositingRenderer:
@Composable
fun <T> StateFlow<T>.collectAsStateWithLifecycle(
    scope: CoroutineScope, // Use the scope from the renderer
    initial: T = this.value // Use current value as initial to avoid flicker
): State<T> {
    val state = remember(this, scope) { mutableStateOf(initial) }
    LaunchedEffect(this, scope) {
        scope.launch {
            collect { value ->
                state.value = value
            }
        }
    }
    return state
}
