package com.example.pbsalogisticsapp.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // If Josh is running the server locally on port 3000, use this for the emulator:
    private const val BASE_URL = "http://10.0.2.2:3000/api/"

    // If Josh deployed it to the internet, put his live URL here instead.

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}