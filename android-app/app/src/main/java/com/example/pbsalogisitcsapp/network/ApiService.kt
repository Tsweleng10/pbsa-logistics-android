package com.example.pbsalogisticsapp.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    // Notice the @Header("Authorization") - Josh's requireAuth middleware needs this!
    @POST("eld/clock-in")
    suspend fun clockIn(
        @Header("Authorization") token: String,
        @Body request: ClockInRequest
    ): Response<EldLog>

    @POST("eld/clock-out")
    suspend fun clockOut(
        @Header("Authorization") token: String,
        @Body request: ClockOutRequest
    ): Response<EldLog>

    @GET("eld/my")
    suspend fun getMyLogs(
        @Header("Authorization") token: String
    ): Response<List<EldLog>>
}