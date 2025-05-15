package com.example.service

import com.example.db.UserDAO
import com.example.db.UserTable
import com.example.db.daoToModel
import com.example.db.suspendTransaction
import com.example.model.User
import com.example.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseToken
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere


class UserService: UserRepository {

    override suspend fun getAllUsers(): List<User> = suspendTransaction {
        UserDAO.all().map(::daoToModel)
    }

    override suspend fun userById(id: String): User? = suspendTransaction {
        UserDAO
            .find{(UserTable.id eq id)}
            .limit(1)
            .map(:: daoToModel)
            .firstOrNull()
    }

    override suspend fun createUser(user: User): Unit = suspendTransaction {
        UserDAO.new(user.id) {
            name = user.name
            email = user.email
            photo = user.photo
        }
    }

    override suspend fun deleteUser(id: String): Boolean = suspendTransaction{
        val rowsDelated = UserTable.deleteWhere{
            UserTable.id eq id
        }
        rowsDelated == 1
    }

    suspend fun registerUserFromFirebase(token: String, name: String){
        val decodedToken: FirebaseToken = FirebaseAuth.getInstance().verifyIdToken(token)
        val uid = decodedToken.uid
        val email = decodedToken.email ?: "sin_email"
        val photo = decodedToken.picture

        val user = User(
            id = uid,
            name = name,
            email = email,
            photo = photo
        )

        createUser(user)
    }
}