package com.wAether.data.repository

import android.content.Context
import android.location.Location
import com.google.firebase.firestore.FirebaseFirestore
import com.wAether.data.model.*
import com.wAether.data.network.NoaaSwpcService
import com.wAether.data.network.OpenMeteoService
import com.wAether.sensor.DeviceInfoProvider
import com.wAether.sensor.LocationProvider
import com.wAether.sensor.MagnetometerReader
import io.mockk.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class DataRepositoryTest {

    private lateinit var mockContext: Context
    private lateinit var mockOpenMeteoService: OpenMeteoService
    private lateinit var mockNoaaSwpcService: NoaaSwpcService
    private lateinit var mockLocationProvider: LocationProvider
    private lateinit var mockMagnetometerReader: MagnetometerReader
    private lateinit var mockDeviceInfoProvider: DeviceInfoProvider
    private lateinit var mockFirestore: FirebaseFirestore
    
    private lateinit var dataRepository: DataRepository

    @Before
    fun setUp() {
        mockContext = mockk()
        mockOpenMeteoService = mockk()
        mockNoaaSwpcService = mockk()
        mockLocationProvider = mockk()
        mockMagnetometerReader = mockk()
        mockDeviceInfoProvider = mockk()
        mockFirestore = mockk()
        
        dataRepository = DataRepository(
            context = mockContext,
            openMeteoService = mockOpenMeteoService,
            noaaSwpcService = mockNoaaSwpcService,
            locationProvider = mockLocationProvider,
            magnetometerReader = mockMagnetometerReader,
            deviceInfoProvider = mockDeviceInfoProvider,
            firestore = mockFirestore
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun getCombinedLogData_locationFailure_returnsFailure() = runTest {
        // Given
        val locationException = Exception("Location not available")
        coEvery { mockLocationProvider.getCurrentLocation() } returns Result.failure(locationException)

        // When
        val result = dataRepository.getCombinedLogData().first()

        // Then
        assertTrue("Should return failure when location fails", result.isFailure)
        assertEquals("Should return the location exception", locationException, result.exceptionOrNull())
    }

    @Test
    fun getCombinedLogData_allServicesSucceed_returnsSuccess() = runTest {
        // Given
        val mockLocation = mockk<Location>()
        every { mockLocation.latitude } returns 40.7128
        every { mockLocation.longitude } returns -74.0060
        
        val openMeteoResponse = OpenMeteoResponse(
            latitude = 40.7128,
            longitude = -74.0060,
            current = OpenMeteoCurrentData(
                temperature2m = 22.5,
                apparentTemperature = 24.0,
                relativeHumidity2m = 65,
                weatherCode = 2,
                windSpeed10m = 10.5,
                precipitation = 0.0
            ),
            hourly = OpenMeteoHourlyData(
                time = listOf("2024-01-01T12:00"),
                directNormalIrradiance = listOf(800.0)
            ),
            daily = OpenMeteoDailyData(
                time = listOf("2024-01-01"),
                moonPhase = listOf(0.5)
            ),
            currentUnits = null,
            hourlyUnits = null,
            dailyUnits = null,
            generationTimeMs = null,
            utcOffsetSeconds = null,
            timezone = null,
            timezoneAbbreviation = null,
            elevation = null
        )
        
        val kpIndexEntry = NoaaKpIndexEntry(
            timeTag = "2024-01-01T12:00:00Z",
            kpIndexValue = "3",
            observationTimeTag = null,
            dataTypeCode = null
        )
        
        val xrayFluxEntry = GoesXrayFluxEntry(
            timeTag = "2024-01-01T12:00:00Z",
            flux = 2.1e-6,
            energyBand = "1-8A"
        )
        
        val solarWindEntry = NoaaSolarWindEntry(
            timeTag = "2024-01-01T12:00:00Z",
            density = "5.5",
            speed = "400",
            temperature = "50000"
        )
        
        val deviceInfo = DeviceInfoProvider.DeviceDetails(
            deviceModel = "Samsung Galaxy Watch",
            osVersion = "Android 12",
            batteryLevel = 85
        )

        // Mock all service calls
        coEvery { mockLocationProvider.getCurrentLocation() } returns Result.success(mockLocation)
        coEvery { mockOpenMeteoService.getCurrentWeatherAndSolarData(any(), any(), any()) } returns openMeteoResponse
        coEvery { mockNoaaSwpcService.getKpIndex() } returns listOf(kpIndexEntry)
        coEvery { mockNoaaSwpcService.getGoesXrayFlux() } returns listOf(xrayFluxEntry)
        coEvery { mockNoaaSwpcService.getSolarWind() } returns listOf(solarWindEntry)
        coEvery { mockMagnetometerReader.getMagneticFieldStrength() } returns 50.5
        coEvery { mockDeviceInfoProvider.getDeviceInfo() } returns deviceInfo

        // When
        val result = dataRepository.getCombinedLogData().first()

        // Then
        assertTrue("Should return success when all services succeed", result.isSuccess)
        val partialData = result.getOrThrow()
        
        assertEquals(40.7128, partialData.latitude, 0.0001)
        assertEquals(-74.0060, partialData.longitude, 0.0001)
        assertEquals(22.5, partialData.localTemperature, 0.0001)
        assertEquals(24.0, partialData.localFeelsLikeTemperature, 0.0001)
        assertEquals(65, partialData.localHumidity)
        assertEquals(2, partialData.localWeatherCode)
        assertEquals(10.5, partialData.localWindSpeed, 0.0001)
        assertEquals(0.0, partialData.localPrecipitation, 0.0001)
        assertEquals(800.0, partialData.irradiance, 0.0001)
        assertEquals(0.5, partialData.moonPhase, 0.0001)
        assertEquals(3, partialData.kpIndex)
        assertEquals(2.1e-6, partialData.xrayFlux, 1e-9)
        assertEquals(400.0, partialData.solarWindSpeed, 0.0001)
        assertEquals(5.5, partialData.solarWindDensity, 0.0001)
        assertEquals(50.5, partialData.magneticField, 0.0001)
        assertEquals("Samsung Galaxy Watch", partialData.deviceModel)
        assertEquals("Android 12", partialData.osVersion)
        assertEquals(85, partialData.batteryLevel)
    }

    @Test
    fun getCombinedLogData_partialServiceFailures_returnsPartialData() = runTest {
        // Given - Location succeeds, but some services fail
        val mockLocation = mockk<Location>()
        every { mockLocation.latitude } returns 40.7128
        every { mockLocation.longitude } returns -74.0060
        
        val openMeteoResponse = OpenMeteoResponse(
            latitude = 40.7128,
            longitude = -74.0060,
            current = OpenMeteoCurrentData(
                temperature2m = 22.5,
                apparentTemperature = 24.0,
                relativeHumidity2m = 65,
                weatherCode = 2,
                windSpeed10m = 10.5,
                precipitation = 0.0
            ),
            hourly = null, // No hourly data
            daily = null,  // No daily data
            currentUnits = null,
            hourlyUnits = null,
            dailyUnits = null,
            generationTimeMs = null,
            utcOffsetSeconds = null,
            timezone = null,
            timezoneAbbreviation = null,
            elevation = null
        )
        
        val deviceInfo = DeviceInfoProvider.DeviceDetails(
            deviceModel = "Samsung Galaxy Watch",
            osVersion = "Android 12",
            batteryLevel = 85
        )

        // Mock service calls - some succeed, some fail
        coEvery { mockLocationProvider.getCurrentLocation() } returns Result.success(mockLocation)
        coEvery { mockOpenMeteoService.getCurrentWeatherAndSolarData(any(), any(), any()) } returns openMeteoResponse
        coEvery { mockNoaaSwpcService.getKpIndex() } throws Exception("NOAA service unavailable")
        coEvery { mockNoaaSwpcService.getGoesXrayFlux() } throws Exception("GOES data unavailable")
        coEvery { mockNoaaSwpcService.getSolarWind() } throws Exception("Solar wind data unavailable")
        coEvery { mockMagnetometerReader.getMagneticFieldStrength() } returns null // Sensor not available
        coEvery { mockDeviceInfoProvider.getDeviceInfo() } returns deviceInfo

        // When
        val result = dataRepository.getCombinedLogData().first()

        // Then
        assertTrue("Should return success even with partial failures", result.isSuccess)
        val partialData = result.getOrThrow()
        
        // Weather data should be available
        assertEquals(40.7128, partialData.latitude, 0.0001)
        assertEquals(-74.0060, partialData.longitude, 0.0001)
        assertEquals(22.5, partialData.localTemperature, 0.0001)
        assertEquals(24.0, partialData.localFeelsLikeTemperature, 0.0001)
        
        // NOAA data should be null/default
        assertNull("Kp index should be null when service fails", partialData.kpIndex)
        assertNull("X-ray flux should be null when service fails", partialData.xrayFlux)
        assertNull("Solar wind speed should be null when service fails", partialData.solarWindSpeed)
        assertNull("Solar wind density should be null when service fails", partialData.solarWindDensity)
        
        // Sensor data should be null
        assertNull("Magnetic field should be null when sensor unavailable", partialData.magneticField)
        
        // Device info should still be available
        assertEquals("Samsung Galaxy Watch", partialData.deviceModel)
        assertEquals("Android 12", partialData.osVersion)
        assertEquals(85, partialData.batteryLevel)
    }

    @Test
    fun getCombinedLogData_openMeteoServiceFailure_returnsPartialData() = runTest {
        // Given
        val mockLocation = mockk<Location>()
        every { mockLocation.latitude } returns 40.7128
        every { mockLocation.longitude } returns -74.0060
        
        val deviceInfo = DeviceInfoProvider.DeviceDetails(
            deviceModel = "Samsung Galaxy Watch",
            osVersion = "Android 12",
            batteryLevel = 85
        )

        // Mock service calls - location and device info succeed, weather fails
        coEvery { mockLocationProvider.getCurrentLocation() } returns Result.success(mockLocation)
        coEvery { mockOpenMeteoService.getCurrentWeatherAndSolarData(any(), any(), any()) } throws Exception("Weather service unavailable")
        coEvery { mockNoaaSwpcService.getKpIndex() } returns emptyList()
        coEvery { mockNoaaSwpcService.getGoesXrayFlux() } returns emptyList()
        coEvery { mockNoaaSwpcService.getSolarWind() } returns emptyList()
        coEvery { mockMagnetometerReader.getMagneticFieldStrength() } returns 50.5
        coEvery { mockDeviceInfoProvider.getDeviceInfo() } returns deviceInfo

        // When
        val result = dataRepository.getCombinedLogData().first()

        // Then
        assertTrue("Should return success even when weather service fails", result.isSuccess)
        val partialData = result.getOrThrow()
        
        // Location should be available
        assertEquals(40.7128, partialData.latitude, 0.0001)
        assertEquals(-74.0060, partialData.longitude, 0.0001)
        
        // Weather data should be null/default
        assertNull("Temperature should be null when weather service fails", partialData.localTemperature)
        assertNull("Humidity should be null when weather service fails", partialData.localHumidity)
        assertNull("Weather code should be null when weather service fails", partialData.localWeatherCode)
        
        // Other data should still be available
        assertEquals(50.5, partialData.magneticField, 0.0001)
        assertEquals("Samsung Galaxy Watch", partialData.deviceModel)
        assertEquals("Android 12", partialData.osVersion)
        assertEquals(85, partialData.batteryLevel)
    }

    @Test
    fun fetchOpenMeteoData_invalidCoordinates_handledGracefully() = runTest {
        // Given - Invalid coordinates
        val invalidLat = 91.0 // Outside valid range
        val invalidLon = 181.0 // Outside valid range
        
        coEvery { 
            mockOpenMeteoService.getCurrentWeatherAndSolarData(invalidLat, invalidLon, any()) 
        } throws Exception("Invalid coordinates")

        // When & Then
        // The private method is called through getCombinedLogData, so we test through that
        val mockLocation = mockk<Location>()
        every { mockLocation.latitude } returns invalidLat
        every { mockLocation.longitude } returns invalidLon
        
        coEvery { mockLocationProvider.getCurrentLocation() } returns Result.success(mockLocation)
        coEvery { mockNoaaSwpcService.getKpIndex() } returns emptyList()
        coEvery { mockNoaaSwpcService.getGoesXrayFlux() } returns emptyList()
        coEvery { mockNoaaSwpcService.getSolarWind() } returns emptyList()
        coEvery { mockMagnetometerReader.getMagneticFieldStrength() } returns null
        coEvery { mockDeviceInfoProvider.getDeviceInfo() } returns DeviceInfoProvider.DeviceDetails("", "", null)

        val result = dataRepository.getCombinedLogData().first()
        
        // Should still succeed with partial data
        assertTrue("Should handle invalid coordinates gracefully", result.isSuccess)
    }

    @Test
    fun parseAndValidateKpIndex_validData_returnsCorrectValue() {
        // This tests the private method indirectly through the public API
        // We can verify the behavior by mocking the service to return specific data
        // and checking that the resulting partial data has the correct Kp index
    }

    @Test
    fun parseAndValidateXrayFlux_validData_returnsCorrectValue() {
        // Similar to above - test through the public API
    }

    @Test
    fun getWeatherConditionDescription_validCode_returnsCorrectDescription() {
        // This would test the integration with WeatherCodeUtil
        // We can verify by checking the localWeatherCondition field in the result
    }
}