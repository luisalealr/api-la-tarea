package com.example.service

import com.example.db.ScheduleDAO
import com.example.db.ScheduleTable
import com.example.db.SubjectDAO
import com.example.db.SubjectTable
import com.example.db.daoToModel
import com.example.db.scheduleDao
import com.example.db.subjectResponseDao
import com.example.db.utils.suspendTransaction
import com.example.model.ScheduleBlockRequest
import com.example.model.ScheduleBlockResponse
import com.example.repository.ScheduleBlockRepository
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere

class ScheduleBlockService : ScheduleBlockRepository {
    override suspend fun getAllScheduleBlocks(): List<ScheduleBlockResponse> = suspendTransaction {
        ScheduleDAO.all().map(:: scheduleDao)
    }

    override suspend fun scheduleById(id: Int): ScheduleBlockResponse? = suspendTransaction {
        ScheduleDAO
            .find { (ScheduleTable.id eq id) }
            .limit(1)
            .map(:: scheduleDao)
            .firstOrNull()
    }

    override suspend fun createScheduleBlock(schedule: ScheduleBlockRequest): Unit = suspendTransaction {
        ScheduleDAO.new {
            day = schedule.day
            startHour = schedule.startHour
            finishHour = schedule.finishHour
        }
    }

    override suspend fun updateScheduleBlock(id: Int, schedule: ScheduleBlockRequest): Unit = suspendTransaction {
        val scheduleToUpdate = ScheduleDAO.findById(id)
            ?: throw IllegalArgumentException("No se encontró la un bloque con id $id")

        scheduleToUpdate.day = schedule.day
        scheduleToUpdate.startHour = schedule.startHour
        scheduleToUpdate.finishHour = schedule.finishHour
    }

    override suspend fun deleteScheduleBlock(id: Int): Boolean = suspendTransaction {
        val rowsDeleted = ScheduleTable.deleteWhere {
            ScheduleTable.id eq id
        }
        rowsDeleted == 1
    }
}