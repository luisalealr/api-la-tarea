package com.example.repository

import com.example.model.TaskRequest
import com.example.model.TaskResponse

interface TaskRepository {
    suspend fun getAllTask() : List<TaskResponse>
    suspend fun taskById(id: Int): TaskResponse?
    suspend fun createTask(task: TaskRequest)
    suspend fun updateTask(id: Int, task: TaskRequest)
    suspend fun deleteTask(id: Int): Boolean
}