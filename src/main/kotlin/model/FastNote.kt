package com.example.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class FastNote (
    val id: Int,
    val title: String,
    val description: String,
    @Contextual val createdAt: LocalDate,
    val userId : String?,
)