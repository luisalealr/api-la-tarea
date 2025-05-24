package com.example.db

import com.example.model.User
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable

object UserTable : IdTable<String>("User") {
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

fun daoToModel(dao: UserDAO) = User(
    dao.id.value,
    dao.name,
    dao.email,
    dao.photo
)