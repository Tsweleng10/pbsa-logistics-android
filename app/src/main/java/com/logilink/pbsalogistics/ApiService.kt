package com.logilink.pbsalogistics

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.GET

// 1. The blueprint for what we send TO the server
data class LoginRequest(
    val email: String,
    val password: String
)

// 2. The blueprint for the User object the server sends back
data class User(
    val id: Int,
    val full_name: String,
    val email: String,
    val role: String
)

// 3. The blueprint for the full response FROM the server
data class LoginResponse(
    val message: String,
    val user: User,
    val token: String
)

// 4. The actual Retrofit interface
interface ApiService {
    @POST("api/auth/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>
    @PUT("api/auth/settings")
    fun updateSettings(
        @Header("Authorization") token: String,
        @Body request: UpdateSettingsRequest
    ): Call<User>

    // --- DISPATCHER ENDPOINTS ---

    @GET("api/loads")
    fun getAllLoads(@Header("Authorization") token: String): Call<List<Load>>

    @GET("api/eld/all")
    fun getAllELDLogs(@Header("Authorization") token: String): Call<List<ELDLog>>

    @POST("api/loads")
    fun createLoad(@Header("Authorization") token: String, @Body request: CreateLoadRequest): Call<Load>

    @GET("api/loads/my")
    fun getMyLoads(@Header("Authorization") token: String): Call<List<Load>>

    @POST("api/eld/clock-in")
    fun clockIn(@Header("Authorization") token: String, @Body request: ClockInRequest): Call<Any>

    @POST("api/eld/clock-out")
    fun clockOut(@Header("Authorization") token: String, @Body request: ClockOutRequest): Call<Any>
}

data class UpdateSettingsRequest(
    val full_name: String?,
    val phone: String?
)

data class Load(
    val id: Int,
    val reference: String,
    val driver_name: String?,
    val origin: String,
    val destination: String,
    val status: String
)

data class ELDLog(
    val id: Int,
    val driver_name: String,
    val duty_status: String,
    val start_time: String
)

data class CreateLoadRequest(
    val reference: String,
    val driver_id: Int,
    val origin: String,
    val destination: String,
    val cargo_description: String,
    val weight_kg: Int
)

data class ClockInRequest(val duty_status: String, val location: String, val load_id: Int?)
data class ClockOutRequest(val notes: String)