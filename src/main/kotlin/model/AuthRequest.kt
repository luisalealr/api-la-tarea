package com.example.model

import kotlinx.serialization.Serializable

@Serializable
data class AuthRequest (
    val token: String,
    val name: String
)