package com.example.pbsalogisticsapp.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("auth/login") // Josh's route file is auth.js
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
}