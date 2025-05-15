package com.example.db

import com.example.model.User
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

object UserTable : IdTable<String>("user") {
    override val id = varchar("id", 128).entityId() // el UID de Firebase
    val name = varchar("name", 100)
    val email = varchar("email", 100)
    val photo = varchar("photo", 200).nullable()
}

class UserDAO(id: EntityID<String>) : Entity<String>(id) {
    companion object : EntityClass<String, UserDAO>(UserTable)

    var name by UserTable.name
    var email by UserTable.email
    var photo by UserTable.photo
}

suspend fun <T> suspendTransaction(block: Transaction.() -> T): T =
    newSuspendedTransaction(Dispatchers.IO, statement = block)

fun daoToModel(dao: UserDAO) = User(
    dao.id.value,
    dao.name,
    dao.email,
    dao.photo
)