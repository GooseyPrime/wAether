package com.wAether.sensor

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build
import io.mockk.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.TIRAMISU])
class DeviceInfoProviderTest {

    private lateinit var mockContext: Context
    private lateinit var deviceInfoProvider: DeviceInfoProvider

    @Before
    fun setUp() {
        mockContext = mockk()
        deviceInfoProvider = DeviceInfoProvider(mockContext)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun getDeviceInfo_withValidBatteryIntent_returnsCorrectDeviceDetails() {
        // Mock battery intent
        val mockBatteryIntent = mockk<Intent>()
        every { mockBatteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) } returns 75
        every { mockBatteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1) } returns 100
        
        every { 
            mockContext.registerReceiver(null, any<IntentFilter>()) 
        } returns mockBatteryIntent

        val deviceInfo = deviceInfoProvider.getDeviceInfo()

        assertNotNull(deviceInfo)
        assertTrue("Device model should not be empty", deviceInfo.deviceModel.isNotEmpty())
        assertTrue("OS version should contain 'Android'", deviceInfo.osVersion.contains("Android"))
        assertEquals("Battery level should be 75%", 75, deviceInfo.batteryLevel)
    }

    @Test
    fun getDeviceInfo_withNullBatteryIntent_returnsNullBatteryLevel() {
        every { 
            mockContext.registerReceiver(null, any<IntentFilter>()) 
        } returns null

        val deviceInfo = deviceInfoProvider.getDeviceInfo()

        assertNotNull(deviceInfo)
        assertTrue("Device model should not be empty", deviceInfo.deviceModel.isNotEmpty())
        assertTrue("OS version should contain 'Android'", deviceInfo.osVersion.contains("Android"))
        assertNull("Battery level should be null when intent is null", deviceInfo.batteryLevel)
    }

    @Test
    fun getDeviceInfo_withInvalidBatteryData_returnsNullBatteryLevel() {
        val mockBatteryIntent = mockk<Intent>()
        every { mockBatteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) } returns -1
        every { mockBatteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1) } returns -1
        
        every { 
            mockContext.registerReceiver(null, any<IntentFilter>()) 
        } returns mockBatteryIntent

        val deviceInfo = deviceInfoProvider.getDeviceInfo()

        assertNotNull(deviceInfo)
        assertNull("Battery level should be null when data is invalid", deviceInfo.batteryLevel)
    }

    @Test
    fun getDeviceInfo_withZeroScale_returnsNullBatteryLevel() {
        val mockBatteryIntent = mockk<Intent>()
        every { mockBatteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) } returns 50
        every { mockBatteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1) } returns 0
        
        every { 
            mockContext.registerReceiver(null, any<IntentFilter>()) 
        } returns mockBatteryIntent

        val deviceInfo = deviceInfoProvider.getDeviceInfo()

        assertNotNull(deviceInfo)
        assertNull("Battery level should be null when scale is 0", deviceInfo.batteryLevel)
    }

    @Test
    fun getDeviceInfo_deviceModelContainsManufacturerAndModel() {
        every { 
            mockContext.registerReceiver(null, any<IntentFilter>()) 
        } returns null

        val deviceInfo = deviceInfoProvider.getDeviceInfo()

        assertTrue("Device model should contain manufacturer and model",
            deviceInfo.deviceModel.contains(" "))
        assertFalse("Device model should not be empty", deviceInfo.deviceModel.isBlank())
    }

    @Test
    fun getDeviceInfo_osVersionContainsAndroidAndSDK() {
        every { 
            mockContext.registerReceiver(null, any<IntentFilter>()) 
        } returns null

        val deviceInfo = deviceInfoProvider.getDeviceInfo()

        assertTrue("OS version should contain 'Android'", 
            deviceInfo.osVersion.contains("Android"))
        assertTrue("OS version should contain 'SDK'", 
            deviceInfo.osVersion.contains("SDK"))
        assertTrue("OS version should contain a number", 
            deviceInfo.osVersion.any { it.isDigit() })
    }

    @Test
    fun getDeviceInfo_batteryPercentageCalculation_isCorrect() {
        val testCases = listOf(
            Triple(50, 100, 50),  // 50/100 = 50%
            Triple(75, 100, 75),  // 75/100 = 75%
            Triple(25, 50, 50),   // 25/50 = 50%
            Triple(33, 100, 33),  // 33/100 = 33%
            Triple(99, 100, 99)   // 99/100 = 99%
        )

        testCases.forEach { (level, scale, expectedPercentage) ->
            val mockBatteryIntent = mockk<Intent>()
            every { mockBatteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) } returns level
            every { mockBatteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1) } returns scale
            
            every { 
                mockContext.registerReceiver(null, any<IntentFilter>()) 
            } returns mockBatteryIntent

            val deviceInfo = deviceInfoProvider.getDeviceInfo()

            assertEquals("Battery percentage calculation incorrect for $level/$scale",
                expectedPercentage, deviceInfo.batteryLevel)
        }
    }

    @Test
    fun getDeviceInfo_multipleCallsReturnConsistentDeviceInfo() {
        every { 
            mockContext.registerReceiver(null, any<IntentFilter>()) 
        } returns null

        val deviceInfo1 = deviceInfoProvider.getDeviceInfo()
        val deviceInfo2 = deviceInfoProvider.getDeviceInfo()

        assertEquals("Device model should be consistent", deviceInfo1.deviceModel, deviceInfo2.deviceModel)
        assertEquals("OS version should be consistent", deviceInfo1.osVersion, deviceInfo2.osVersion)
        // Battery level might differ between calls in real usage, but for our mock it should be the same
        assertEquals("Battery level should be consistent with mock", deviceInfo1.batteryLevel, deviceInfo2.batteryLevel)
    }

    @Test
    fun deviceDetails_dataClass_equality() {
        val details1 = DeviceInfoProvider.DeviceDetails("Samsung Galaxy", "Android 12", 80)
        val details2 = DeviceInfoProvider.DeviceDetails("Samsung Galaxy", "Android 12", 80)
        val details3 = DeviceInfoProvider.DeviceDetails("Samsung Galaxy", "Android 12", 85)

        assertEquals("Same DeviceDetails should be equal", details1, details2)
        assertNotEquals("Different battery levels should not be equal", details1, details3)
    }

    @Test
    fun deviceDetails_dataClass_hashCode() {
        val details1 = DeviceInfoProvider.DeviceDetails("Samsung Galaxy", "Android 12", 80)
        val details2 = DeviceInfoProvider.DeviceDetails("Samsung Galaxy", "Android 12", 80)

        assertEquals("Same DeviceDetails should have same hashCode", 
            details1.hashCode(), details2.hashCode())
    }

    @Test
    fun deviceDetails_dataClass_toString() {
        val details = DeviceInfoProvider.DeviceDetails("Samsung Galaxy", "Android 12", 80)
        val stringResult = details.toString()

        assertTrue("toString should contain device model", stringResult.contains("Samsung Galaxy"))
        assertTrue("toString should contain OS version", stringResult.contains("Android 12"))
        assertTrue("toString should contain battery level", stringResult.contains("80"))
    }
}