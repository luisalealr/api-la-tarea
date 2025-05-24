package com.example.model

import kotlinx.serialization.Serializable

@Serializable
data class ScheduleBlock(
    val id: Int,
    val day : String,
    val startHour: String,
    val finishHour: String,
)