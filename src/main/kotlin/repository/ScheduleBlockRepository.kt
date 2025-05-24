package com.example.repository

import com.example.model.ScheduleBlock

interface ScheduleBlockRepository {
    suspend fun getAllScheduleBlocks() : List<ScheduleBlock>
    suspend fun scheduleById(id: Int): ScheduleBlock?
    suspend fun createScheduleBlock(schedule: ScheduleBlock)
    suspend fun updateScheduleBlock(id: Int, schedule: ScheduleBlock)
    suspend fun deleteScheduleBlock(id: Int): Boolean
}