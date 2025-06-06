package com.example.model

import kotlinx.serialization.Serializable

@Serializable
data class ScheduleBlockResponse(
    val id: Int,
    val day : String,
    val startHour: String,
    val finishHour: String,
)

@Serializable
data class ScheduleBlockRequest(
    val day : String,
    val startHour: String,
    val finishHour: String,
)