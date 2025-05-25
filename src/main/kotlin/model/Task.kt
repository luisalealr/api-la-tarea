package com.example.model

import kotlinx.serialization.Serializable

@Serializable
data class TaskRequest(
    val title: String,
    val description: String,
    val createdAt: String,
    val expiresAt: String? = null,
    val colorHexa: String? = null,
    val priority: Int? = null,
    val userId: String?= null,
    val subjectId: Int? = null
)

@Serializable
data class TaskResponse(
    val id: Int,
    val title: String,
    val description: String,
    val createdAt: String,
    val expiresAt: String? = null,
    val colorHexa: String? = null,
    val priority: Int? = null,
    val userId: String?= null,
    val subjectId: Int? = null
)

@Serializable
data class TaskUpdate(
    val title: String,
    val description: String,
    val expiresAt: String? = null,
    val colorHexa: String? = null,
    val priority: Int? = null,
    val subjectId: Int? = null
)