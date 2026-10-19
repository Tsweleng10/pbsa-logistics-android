package com.example.pbsalogisticsapp.network

data class LoginRequest(
    val email: String,
    val password: String
)

// Based on Josh's response on line 99-103
data class LoginResponse(
    val message: String,
    val user: User,
    val token: String
)

// Based on the database columns Josh is returning
data class User(
    val id: Int,
    val full_name: String,
    val email: String,
    val role: String,
    val phone: String?,
    val license_number: String?
)
// Add this inside the network package (at the bottom of AuthModels.kt)

data class ClockInRequest(
    val load_id: Int? = null,
    val duty_status: String, // e.g., "Driving", "On Duty"
    val location: String? = null
)

data class ClockOutRequest(
    val notes: String? = null
)

data class EldLog(
    val id: Int,
    val driver_id: Int,
    val load_id: Int?,
    val load_reference: String?, // From the SQL JOIN on line 74
    val duty_status: String,
    val location: String?,
    val start_time: String,
    val end_time: String?,
    val notes: String?
)