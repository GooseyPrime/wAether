package com.wAether.data.network

import retrofit2.Response
import retrofit2.http.GET

/**
 * NOAA Space Weather Prediction Center API service
 */
interface NoaaSwpcService {
    @GET("planetary_k_index_1m.json")
    suspend fun getCurrentKpIndex(): Response<List<List<String>>>
    
    @GET("goes_xray_flux_primary_1m.json")
    suspend fun getXrayFlux(): Response<List<List<String>>>
    
    // Alternative method names for compatibility
    @GET("planetary_k_index_1m.json")
    suspend fun getPlanetaryKpIndex(): Response<List<List<String>>>
    
    @GET("goes_xray_flux_primary_1m.json")
    suspend fun getGoesXrayFlux(): Response<List<List<String>>>
    
    @GET("real_time_solar_wind_1m.json")
    suspend fun getRealTimeSolarWind(): Response<List<List<String>>>
}

/**
 * Data classes for NOAA SWPC API responses
 */
data class KpIndexData(
    val time_tag: String,
    val kp_index: Double
)

data class XrayFluxData(
    val time_tag: String,
    val flux: Double,
    val observed_flux: Double
)

data class SolarWindData(
    val time_tag: String,
    val speed: Double,
    val density: Double
)