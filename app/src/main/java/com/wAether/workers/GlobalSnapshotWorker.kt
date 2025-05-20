package com.wAether.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.wAether.data.repository.DataRepository
import kotlinx.coroutines.flow.firstOrNull

private const val TAG = "GlobalSnapshotWorker"

class GlobalSnapshotWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    // In a real app, use Dependency Injection (e.g., Hilt) to get DataRepository
    // For simplicity here, we instantiate it directly.
    // This requires DataRepository to have a constructor that can be satisfied here,
    // or you'd need a custom WorkManager Factory.
    private val dataRepository = DataRepository(applicationContext)

    override suspend fun doWork(): Result {
        Log.d(TAG, "Starting global snapshot work.")
        return try {
            val partialDataResult = dataRepository.getCombinedLogData().firstOrNull() // Collect the first emission

            if (partialDataResult == null || partialDataResult.isFailure) {
                val errorMsg = "Failed to get combined log data: ${partialDataResult?.exceptionOrNull()?.message ?: "Unknown error"}"
                Log.e(TAG, errorMsg)
                if (runAttemptCount < 3) { // Retry a few times for transient errors
                    Result.retry()
                } else {
                    Result.failure()
                }
            } else {
                val partialData = partialDataResult.getOrThrow()
                // Create a LogEntry for a global snapshot (mood is null)
                val globalSnapshotEntry = partialData.toLogEntry(mood = null)

                Log.d(TAG, "Attempting to save global snapshot: ${globalSnapshotEntry.timestamp}")
                val saveResult = dataRepository.saveLogEntryToFirebase(globalSnapshotEntry)

                if (saveResult.isSuccess) {
                    Log.i(TAG, "Global snapshot successfully saved to Firebase.")
                    Result.success()
                } else {
                    Log.e(TAG, "Failed to save global snapshot to Firebase: ${saveResult.exceptionOrNull()?.message}")
                    if (runAttemptCount < 3) { // Retry for Firebase save failures too
                         Result.retry()
                    } else {
                        Result.failure()
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception in GlobalSnapshotWorker", e)
            if (runAttemptCount < 3) Result.retry() else Result.failure()
        }
    }
}
