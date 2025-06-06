package com.example.model

import kotlinx.serialization.Serializable

@Serializable
data class AuthRequest (
    val token: String,
    val name: String
)

@Serializable
data class LoginRequest(val token: String)

@Serializable
data class LoginResponse(
    val success: Boolean,
    val message: String,
    val uid: String? = null,
    val email: String? = null
)
