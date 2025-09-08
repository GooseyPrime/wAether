package com.wAether.workers

import android.content.Context
import android.util.Log
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

private const val TAG = "SnapshotScheduler"
private const val GLOBAL_SNAPSHOT_WORK_TAG = "globalSnapshotWork"

object SnapshotScheduler {

    // Default: 30 minutes
    private var currentIntervalMinutes = 30L

    fun schedulePeriodicGlobalSnapshots(context: Context) {
        Log.d(TAG, "Scheduling periodic global snapshots with interval: $currentIntervalMinutes minutes.")

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED) // Only run when network is available
            // Add other constraints if needed (e.g., battery not low)
            // .setRequiresBatteryNotLow(true)
            .build()

        val periodicWorkRequest =
            PeriodicWorkRequestBuilder<GlobalSnapshotWorker>(currentIntervalMinutes, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .addTag(GLOBAL_SNAPSHOT_WORK_TAG)
                .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            GLOBAL_SNAPSHOT_WORK_TAG,
            ExistingPeriodicWorkPolicy.KEEP, // Or REPLACE if you want to update the interval
            periodicWorkRequest
        )
        Log.i(TAG, "Enqueued periodic global snapshot work.")
    }

    /**
     * Call this to update the snapshot interval.
     * For example, when Kp Index changes or sleep mode is detected.
     * This will replace the existing periodic work with the new interval.
     */
    fun updateSnapshotInterval(context: Context, newIntervalMinutes: Long) {
        if (newIntervalMinutes == currentIntervalMinutes && isWorkScheduled(context)) {
            Log.d(TAG, "Interval $newIntervalMinutes min is already set. No change needed.")
            return
        }
        currentIntervalMinutes = newIntervalMinutes
        Log.i(TAG, "Updating snapshot interval to $currentIntervalMinutes minutes.")

        // To update, we cancel the old one and enqueue a new one,
        // or use REPLACE policy which does this.
        // For simplicity with REPLACE, we just call schedule again.
        // Ensure ExistingPeriodicWorkPolicy.REPLACE is used if interval changes are frequent.
        // For now, KEEP is fine, but if you call this often, consider REPLACE.
        // Let's switch to REPLACE for dynamic updates.

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val periodicWorkRequest =
            PeriodicWorkRequestBuilder<GlobalSnapshotWorker>(currentIntervalMinutes, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .addTag(GLOBAL_SNAPSHOT_WORK_TAG)
                .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            GLOBAL_SNAPSHOT_WORK_TAG,
            ExistingPeriodicWorkPolicy.REPLACE, // REPLACE to update the existing work with the new interval
            periodicWorkRequest
        )
        Log.i(TAG, "Re-enqueued periodic global snapshot work with new interval.")
    }

    fun cancelPeriodicGlobalSnapshots(context: Context) {
        WorkManager.getInstance(context).cancelUniqueWork(GLOBAL_SNAPSHOT_WORK_TAG)
        Log.i(TAG, "Cancelled periodic global snapshot work.")
    }

    private fun isWorkScheduled(context: Context): Boolean {
        val workManager = WorkManager.getInstance(context)
        val workInfos = workManager.getWorkInfosByTagLiveData(GLOBAL_SNAPSHOT_WORK_TAG).value
        return workInfos?.any { !it.state.isFinished } ?: false
    }

    // TODO: Implement logic to detect Kp Index changes and call updateSnapshotInterval.
    // This might involve the WatchFaceViewModel observing Kp Index and notifying the scheduler,
    // or the scheduler itself having a way to check Kp Index periodically (less ideal).

    // TODO: Implement logic for Sleep Mode detection (e.g., using Android Sleep API if available on Wear OS)
    // and call updateSnapshotInterval.
    // Example:
    // fun onHighKpIndexDetected(context: Context) {
    //     updateSnapshotInterval(context, 10L) // e.g., 10 minutes
    // }
    // fun onSleepModeActive(context: Context, isActive: Boolean) {
    //     if (isActive) {
    //         updateSnapshotInterval(context, 90L) // e.g., 90 minutes
    //     } else {
    //         updateSnapshotInterval(context, 30L) // Back to default
    //     }
    // }
}
