package com.example.repository

import com.example.model.ScheduleBlockRequest
import com.example.model.ScheduleBlockResponse

interface ScheduleBlockRepository {
    suspend fun getAllScheduleBlocks() : List<ScheduleBlockResponse>
    suspend fun scheduleById(id: Int): ScheduleBlockResponse?
    suspend fun createScheduleBlock(schedule: ScheduleBlockRequest)
    suspend fun updateScheduleBlock(id: Int, schedule: ScheduleBlockRequest)
    suspend fun deleteScheduleBlock(id: Int): Boolean
}