package com.example.service

import com.example.db.ScheduleDAO
import com.example.db.SubjectDAO
import com.example.db.SubjectTable
import com.example.db.UserDAO
import com.example.db.subjectResponseDao
import com.example.db.utils.suspendTransaction
import com.example.model.SubjectRequest
import com.example.model.SubjectResponse
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import com.example.repository.SubjectRepository
import org.jetbrains.exposed.sql.deleteWhere

class SubjectService : SubjectRepository {
    override suspend fun getAllSubject(): List<SubjectResponse> = suspendTransaction {
        SubjectDAO.all().map(:: subjectResponseDao)
    }

    override suspend fun subjectById(id: Int): SubjectResponse? = suspendTransaction {
        SubjectDAO
            .find { (SubjectTable.id eq id) }
            .limit(1)
            .map(:: subjectResponseDao)
            .firstOrNull()
    }

    override suspend fun createSubject(subject: SubjectRequest): Unit = suspendTransaction {
        val userDao = subject.userId?.let { UserDAO.findById(it) }
        val scheduleBlockDao = subject.scheduleBlockId?.let { ScheduleDAO.findById(it) }

        SubjectDAO.new {
            title = subject.title
            colorHexa = subject.colorHexa
            user = userDao
            schedule = scheduleBlockDao
        }
    }

    override suspend fun updateSubject(id: Int, subject: SubjectRequest): Unit = suspendTransaction {
        val subjectToUpdate = SubjectDAO.findById(id)
            ?: throw IllegalArgumentException("No se encontró la materia con id $id")

        val scheduleDao = subject.scheduleBlockId?.let { ScheduleDAO.findById(it) }

        subjectToUpdate.title = subject.title
        subjectToUpdate.colorHexa = subject.colorHexa
        subjectToUpdate.schedule = scheduleDao
    }

    override suspend fun deleteSubject(id: Int): Boolean = suspendTransaction {
        val rowsDeleted = SubjectTable.deleteWhere {
            SubjectTable.id eq id
        }
        rowsDeleted == 1
    }

    override suspend fun findSubjectByUser(id: String?): List<SubjectResponse> = suspendTransaction  {
        SubjectDAO
            .find { (SubjectTable.userId eq id) }
            .map(:: subjectResponseDao)
    }
}