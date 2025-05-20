package com.wAether.service

import android.view.SurfaceHolder
import androidx.wear.watchface.ComplicationSlotsManager
import androidx.wear.watchface.WatchFace
import androidx.wear.watchface.WatchFaceService
import androidx.wear.watchface.WatchFaceType
import androidx.wear.watchface.WatchState
import androidx.wear.watchface.style.CurrentUserStyleRepository
import com.wAether.data.repository.DataRepository
import com.wAether.ui.watchface.WatchFaceRenderer // Your existing renderer
import com.wAether.ui.watchface.WatchFaceViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import android.util.Log

private const val TAG = "WAetherWatchFaceService"

/**
 * The main service for the wAether watch face.
 */
class WAetherWatchFaceService : WatchFaceService() {

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    private var watchFaceViewModel: WatchFaceViewModel? = null // Hold a reference for onCleared

    override suspend fun createWatchFace(
        surfaceHolder: SurfaceHolder,
        watchState: WatchState,
        complicationSlotsManager: ComplicationSlotsManager,
        currentUserStyleRepository: CurrentUserStyleRepository
    ): WatchFace {
        Log.d(TAG, "createWatchFace called")

        // Instantiate DataRepository (Context is available from the service)
        val dataRepository = DataRepository(applicationContext)

        // Instantiate WatchFaceViewModel, it's now tied to the serviceScope
        watchFaceViewModel = WatchFaceViewModel(dataRepository, serviceScope)

        // Instantiate your custom renderer
        // IMPORTANT: You will need to update the constructor of your WatchFaceRenderer
        // to accept WatchFaceViewModel and a CoroutineScope.
        val renderer = WatchFaceRenderer(
            context = this, // or applicationContext
            surfaceHolder = surfaceHolder,
            currentUserStyleRepository = currentUserStyleRepository,
            watchState = watchState,
            canvasType = WatchFace.CANVAS_TYPE_HARDWARE,
            complicationSlotsManager = complicationSlotsManager,
            watchFaceViewModel = watchFaceViewModel!!, // Pass the ViewModel (non-null asserted as it's just created)
            scope = serviceScope // Pass the scope for coroutines in the renderer if it needs to launch its own
        )

        // Create and return the WatchFace instance
        return WatchFace(
            watchFaceType = WatchFaceType.DIGITAL,
            renderer = renderer
        ).setTapListener(renderer) // Assuming your renderer implements WatchFace.TapListener
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy called, cancelling serviceScope.")
        watchFaceViewModel?.onCleared() // Call ViewModel's cleanup
        serviceScope.cancel() // Cancel all coroutines started in this scope
        super.onDestroy()
    }
}
