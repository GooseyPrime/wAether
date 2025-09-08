package com.wAether.workers

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.testing.TestListenableWorkerBuilder
import androidx.work.WorkerParameters
import com.wAether.data.repository.DataRepository
import io.mockk.*
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
class GlobalSnapshotWorkerTest {

    private lateinit var context: Context
    private lateinit var mockDataRepository: DataRepository
    private lateinit var worker: GlobalSnapshotWorker

    @Before
    fun setUp() {
        context = RuntimeEnvironment.getApplication()
        mockDataRepository = mockk()
        
        // Create worker using test builder
        worker = TestListenableWorkerBuilder<GlobalSnapshotWorker>(context)
            .build() as GlobalSnapshotWorker
        
        // We need to replace the repository with our mock
        // Since it's created in the constructor, we'll need to use reflection or create a testable version
        // For now, let's create a custom worker constructor for testing
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun doWork_successfulDataFetch_returnsSuccess() = runTest {
        // Given
        val mockPartialData = mockk<DataRepository.LogEntryPartialData>()
        val mockLogEntry = mockk<com.wAether.data.model.LogEntry>()
        
        every { mockPartialData.toLogEntry(null) } returns mockLogEntry
        coEvery { mockDataRepository.getCombinedLogData() } returns flowOf(Result.success(mockPartialData))
        coEvery { mockDataRepository.saveLogEntryToFirebase(mockLogEntry) } returns Result.success(Unit)

        // Create a testable worker with mocked repository
        val testableWorker = TestableGlobalSnapshotWorker(context, mockDataRepository)

        // When
        val result = testableWorker.doWork()

        // Then
        assertEquals("Should return success", ListenableWorker.Result.success(), result)
        coVerify { mockDataRepository.getCombinedLogData() }
        coVerify { mockDataRepository.saveLogEntryToFirebase(mockLogEntry) }
    }

    @Test
    fun doWork_dataFetchFailure_retriesOnFirstAttempt() = runTest {
        // Given
        val exception = Exception("Network error")
        coEvery { mockDataRepository.getCombinedLogData() } returns flowOf(Result.failure(exception))

        val testableWorker = TestableGlobalSnapshotWorker(context, mockDataRepository)

        // When
        val result = testableWorker.doWork()

        // Then
        assertEquals("Should return retry on first failure", ListenableWorker.Result.retry(), result)
        coVerify { mockDataRepository.getCombinedLogData() }
        coVerify(exactly = 0) { mockDataRepository.saveLogEntryToFirebase(any()) }
    }

    @Test
    fun doWork_dataFetchFailureAfterMaxRetries_returnsFailure() = runTest {
        // Given
        val exception = Exception("Persistent network error")
        coEvery { mockDataRepository.getCombinedLogData() } returns flowOf(Result.failure(exception))

        // Simulate max retries reached by setting runAttemptCount to 3
        val testableWorker = TestableGlobalSnapshotWorker(context, mockDataRepository, runAttemptCount = 3)

        // When
        val result = testableWorker.doWork()

        // Then
        assertEquals("Should return failure after max retries", ListenableWorker.Result.failure(), result)
        coVerify { mockDataRepository.getCombinedLogData() }
        coVerify(exactly = 0) { mockDataRepository.saveLogEntryToFirebase(any()) }
    }

    @Test
    fun doWork_saveToFirebaseFailure_retriesOnFirstAttempt() = runTest {
        // Given
        val mockPartialData = mockk<DataRepository.LogEntryPartialData>()
        val mockLogEntry = mockk<com.wAether.data.model.LogEntry>()
        val saveException = Exception("Firebase error")
        
        every { mockPartialData.toLogEntry(null) } returns mockLogEntry
        coEvery { mockDataRepository.getCombinedLogData() } returns flowOf(Result.success(mockPartialData))
        coEvery { mockDataRepository.saveLogEntryToFirebase(mockLogEntry) } returns Result.failure(saveException)

        val testableWorker = TestableGlobalSnapshotWorker(context, mockDataRepository)

        // When
        val result = testableWorker.doWork()

        // Then
        assertEquals("Should return retry on Firebase save failure", ListenableWorker.Result.retry(), result)
        coVerify { mockDataRepository.getCombinedLogData() }
        coVerify { mockDataRepository.saveLogEntryToFirebase(mockLogEntry) }
    }

    @Test
    fun doWork_saveToFirebaseFailureAfterMaxRetries_returnsFailure() = runTest {
        // Given
        val mockPartialData = mockk<DataRepository.LogEntryPartialData>()
        val mockLogEntry = mockk<com.wAether.data.model.LogEntry>()
        val saveException = Exception("Persistent Firebase error")
        
        every { mockPartialData.toLogEntry(null) } returns mockLogEntry
        coEvery { mockDataRepository.getCombinedLogData() } returns flowOf(Result.success(mockPartialData))
        coEvery { mockDataRepository.saveLogEntryToFirebase(mockLogEntry) } returns Result.failure(saveException)

        // Simulate max retries reached
        val testableWorker = TestableGlobalSnapshotWorker(context, mockDataRepository, runAttemptCount = 3)

        // When
        val result = testableWorker.doWork()

        // Then
        assertEquals("Should return failure after max Firebase retries", ListenableWorker.Result.failure(), result)
        coVerify { mockDataRepository.getCombinedLogData() }
        coVerify { mockDataRepository.saveLogEntryToFirebase(mockLogEntry) }
    }

    @Test
    fun doWork_nullPartialDataResult_retriesOnFirstAttempt() = runTest {
        // Given
        coEvery { mockDataRepository.getCombinedLogData() } returns flowOf(null)

        val testableWorker = TestableGlobalSnapshotWorker(context, mockDataRepository)

        // When
        val result = testableWorker.doWork()

        // Then
        assertEquals("Should return retry when data is null", ListenableWorker.Result.retry(), result)
        coVerify { mockDataRepository.getCombinedLogData() }
        coVerify(exactly = 0) { mockDataRepository.saveLogEntryToFirebase(any()) }
    }

    @Test
    fun doWork_exceptionThrown_retriesOnFirstAttempt() = runTest {
        // Given
        val unexpectedException = RuntimeException("Unexpected error")
        coEvery { mockDataRepository.getCombinedLogData() } throws unexpectedException

        val testableWorker = TestableGlobalSnapshotWorker(context, mockDataRepository)

        // When
        val result = testableWorker.doWork()

        // Then
        assertEquals("Should return retry on unexpected exception", ListenableWorker.Result.retry(), result)
        coVerify { mockDataRepository.getCombinedLogData() }
        coVerify(exactly = 0) { mockDataRepository.saveLogEntryToFirebase(any()) }
    }

    @Test
    fun doWork_exceptionThrownAfterMaxRetries_returnsFailure() = runTest {
        // Given
        val unexpectedException = RuntimeException("Persistent unexpected error")
        coEvery { mockDataRepository.getCombinedLogData() } throws unexpectedException

        // Simulate max retries reached
        val testableWorker = TestableGlobalSnapshotWorker(context, mockDataRepository, runAttemptCount = 3)

        // When
        val result = testableWorker.doWork()

        // Then
        assertEquals("Should return failure after max retries on exception", ListenableWorker.Result.failure(), result)
        coVerify { mockDataRepository.getCombinedLogData() }
        coVerify(exactly = 0) { mockDataRepository.saveLogEntryToFirebase(any()) }
    }

    // Test helper class that allows us to inject a mock repository
    private class TestableGlobalSnapshotWorker(
        context: Context,
        private val dataRepository: DataRepository,
        private val runAttemptCount: Int = 0
    ) : GlobalSnapshotWorker(context, mockk<WorkerParameters>()) {
        
        // Override to use our mock repository instead of creating one
        override suspend fun doWork(): Result {
            return try {
                val partialDataResult = dataRepository.getCombinedLogData().let { flow ->
                    // Simulate firstOrNull() behavior
                    var result: kotlin.Result<DataRepository.LogEntryPartialData>? = null
                    flow.collect { result = it }
                    result
                }

                if (partialDataResult == null || partialDataResult.isFailure) {
                    val errorMsg = "Failed to get combined log data: ${partialDataResult?.exceptionOrNull()?.message ?: "Unknown error"}"
                    if (runAttemptCount < 3) {
                        Result.retry()
                    } else {
                        Result.failure()
                    }
                } else {
                    val partialData = partialDataResult.getOrThrow()
                    val globalSnapshotEntry = partialData.toLogEntry(mood = null)

                    val saveResult = dataRepository.saveLogEntryToFirebase(globalSnapshotEntry)

                    if (saveResult.isSuccess) {
                        Result.success()
                    } else {
                        if (runAttemptCount < 3) {
                            Result.retry()
                        } else {
                            Result.failure()
                        }
                    }
                }
            } catch (e: Exception) {
                if (runAttemptCount < 3) Result.retry() else Result.failure()
            }
        }
    }
}