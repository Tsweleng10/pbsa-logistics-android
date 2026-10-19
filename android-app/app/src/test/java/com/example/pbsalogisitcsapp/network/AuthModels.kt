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