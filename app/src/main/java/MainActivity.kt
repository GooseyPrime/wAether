package com.wAether

import android.Manifest
import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.watchface.editor.EditorRequest // For creating editor requests
import androidx.wear.watchface.editor.WatchFaceEditorContract // For launching watch face selection

private const val TAG = "MainActivity"

class MainActivity : ComponentActivity() {

    private val requiredPermissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.BODY_SENSORS
        // Add INTERNET if you need to check it, though it's usually granted at install.
        // FOREGROUND_SERVICE is also critical but handled differently (app needs to start one).
    )

    private var permissionsGranted by mutableStateOf(false)

    private val requestMultiplePermissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissionsGranted = permissions.entries.all { it.value }
            if (permissionsGranted) {
                Log.d(TAG, "All required permissions granted.")
                // Permissions are granted. You can proceed with app logic or just let the watch face run.
            } else {
                Log.w(TAG, "Not all permissions were granted.")
                // Handle the case where permissions are not granted.
                // You might show a message or guide the user to settings.
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkAndRequestPermissions()

        setContent {
            MaterialTheme { // Use your app's Wear OS theme
                MainScreen(
                    permissionsGranted = permissionsGranted,
                    onRequestPermissions = ::checkAndRequestPermissions,
                    onSelectWatchFace = ::launchWatchFaceSelection
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Re-check permissions in case they were changed while the app was paused
        permissionsGranted = checkPermissions()
    }

    private fun checkPermissions(): Boolean {
        return requiredPermissions.all {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun checkAndRequestPermissions() {
        permissionsGranted = checkPermissions()
        if (!permissionsGranted) {
            Log.d(TAG, "Requesting permissions: ${requiredPermissions.joinToString()}")
            requestMultiplePermissionsLauncher.launch(requiredPermissions)
        } else {
            Log.d(TAG, "All permissions already granted.")
        }
    }

    private fun launchWatchFaceSelection() {
        try {
            // Create EditorRequest for the watch face editor
            val editorRequest = EditorRequest(
                ComponentName(packageName, "com.wAether.service.WAetherWatchFaceService"),
                "",
                null
            )
            val watchFaceEditorIntent = WatchFaceEditorContract().createIntent(
                this,
                editorRequest
            )
            startActivity(watchFaceEditorIntent)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to launch watch face editor", e)
            // Fallback or error message
        }
    }
}

@Composable
fun MainScreen(permissionsGranted: Boolean, onRequestPermissions: () -> Unit, onSelectWatchFace: () -> Unit) {
    val context = LocalContext.current as Activity

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("wAether Setup", style = MaterialTheme.typography.title1)
        Spacer(modifier = Modifier.height(16.dp))

        if (permissionsGranted) {
            Text(
                "All set! Select the wAether face from your watch face picker.",
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onSelectWatchFace) {
                Text("Choose Watch Face")
            }
        } else {
            Text(
                "wAether needs permissions for location and sensors to provide all features.",
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onRequestPermissions) {
                Text("Grant Permissions")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = {
                // Open app settings
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", context.packageName, null)
                intent.data = uri
                context.startActivity(intent)
            }) {
                Text("Open App Settings")
            }
        }
    }
}
