package com.example.model

import kotlinx.serialization.Serializable

@Serializable
data class FastNoteResponse (
    val id: Int,
    val title: String,
    val description: String,
    val userId : String? = null,
)

@Serializable
data class FastNoteRequest (
    val title: String,
    val description: String,
    val userId : String? = null,
)