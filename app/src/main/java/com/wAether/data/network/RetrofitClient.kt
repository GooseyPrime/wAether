package com.wAether.data.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Provides Retrofit client instances for accessing APIs.
 * This can be expanded or integrated with a DI framework like Hilt later.
 */
object RetrofitClient {

    // Configure OkHttpClient with a logging interceptor for debugging
    // and appropriate timeouts.
    private val okHttpClient: OkHttpClient by lazy {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY // Log request and response bodies
        }
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS) // Connection timeout
            .readTimeout(30, TimeUnit.SECONDS) // Read timeout
            .writeTimeout(30, TimeUnit.SECONDS) // Write timeout
            .build()
    }

    // Lazy initialization of OpenMeteoService
    val openMeteoService: OpenMeteoService by lazy {
        Retrofit.Builder()
            .baseUrl(ApiConstants.OPEN_METEO_BASE_URL)
            .client(okHttpClient) // Use the shared OkHttpClient
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OpenMeteoService::class.java)
    }

    // Lazy initialization of NoaaSwpcService
    val noaaSwpcService: NoaaSwpcService by lazy {
        Retrofit.Builder()
            .baseUrl(ApiConstants.NOAA_SWPC_BASE_URL)
            .client(okHttpClient) // Use the shared OkHttpClient
            .addConverterFactory(GsonConverterFactory.create()) // Gson should handle List<List<String>> and List<DataClass>
            .build()
            .create(NoaaSwpcService::class.java)
    }
}
