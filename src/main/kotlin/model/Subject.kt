package com.example.model

import kotlinx.serialization.Serializable

@Serializable
data class SubjectResponse(
    val id: Int,
    val title: String,
    val colorHexa: String? = null,
    val userId: String? = null,
    val scheduleBlockId: Int? = null,
)

@Serializable
data class SubjectRequest(
    val title: String,
    val colorHexa: String? = null,
    val userId: String? = null,
    val scheduleBlockId: Int? = null,
)