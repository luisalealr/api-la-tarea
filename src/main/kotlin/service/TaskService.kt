package com.example.service

import com.example.db.SubjectDAO
import com.example.db.TaskDAO
import com.example.db.TaskTable
import com.example.db.UserDAO
import com.example.db.taskResponseDao
import com.example.db.utils.suspendTransaction
import com.example.model.TaskRequest
import com.example.model.TaskResponse
import com.example.repository.TaskRepository
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import java.time.LocalDate

class TaskService : TaskRepository {
    override suspend fun getAllTask(): List<TaskResponse> = suspendTransaction {
        TaskDAO.all().map(:: taskResponseDao)
    }

    override suspend fun taskById(id: Int): TaskResponse? = suspendTransaction {
        TaskDAO
            .find { (TaskTable.id eq id) }
            .limit(1)
            .map(:: taskResponseDao)
            .firstOrNull()
    }

    override suspend fun createTask(task: TaskRequest): Unit = suspendTransaction {
        val userDao = task.userId?.let { UserDAO.findById(it) }
        if (userDao == null) throw IllegalArgumentException("User not found")

        val subjectDAO = task.subjectId?.let { SubjectDAO.findById(it) }

        TaskDAO.new {
            title = task.title
            description = task.description
            createdAt = LocalDate.parse(task.createdAt)
            expiresAt = task.expiresAt?.let { LocalDate.parse(it) }
            colorHexa = task.colorHexa
            priority = task.priority
            user = userDao
            subject = subjectDAO
        }
    }

    override suspend fun updateTask(id: Int, task: TaskRequest): Unit = suspendTransaction  {
        val taskToUpdate = TaskDAO.findById(id)
            ?: throw IllegalArgumentException("No se encontró la tarea con id $id")
        val subjectDAO = task.subjectId?.let { SubjectDAO.findById(it) }

        taskToUpdate.title = task.title
        taskToUpdate.description = task.description
        taskToUpdate.expiresAt = task.expiresAt?.let { LocalDate.parse(it) }
        taskToUpdate.colorHexa = task.colorHexa
        taskToUpdate.priority = task.priority
        taskToUpdate.subject = subjectDAO

    }

    override suspend fun deleteTask(id: Int): Boolean = suspendTransaction {
        val rowsDeleted = TaskTable.deleteWhere {
            TaskTable.id eq id
        }
        rowsDeleted == 1
    }

}