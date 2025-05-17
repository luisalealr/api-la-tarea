package com.example.repository

import com.example.model.User

interface UserRepository {
    suspend fun getAllUsers(): List<User>
    suspend fun userById(id: String): User?
    suspend fun createUser(user: User)
    suspend fun deleteUser(id: String): Boolean
}