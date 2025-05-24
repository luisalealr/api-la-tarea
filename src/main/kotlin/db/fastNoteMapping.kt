package com.example.db

import com.example.model.FastNote
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.javatime.date

object NoteTable : IntIdTable("FastNote") {
    val title = varchar("title", 50)
    val description = text("description")
    val createdAt = date("createdAt")
    val userId = optReference("userId", UserTable.id, onDelete = ReferenceOption.CASCADE )
}

class NoteDAO(id : EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<NoteDAO>(NoteTable)
    var title by NoteTable.title
    var description by NoteTable.description
    var createdAt by NoteTable.createdAt
    var user by UserDAO optionalReferencedOn NoteTable.userId
}

fun daoToModel(dao : NoteDAO) = FastNote (
    dao.id.value,
    dao.title,
    dao.description,
    dao.createdAt,
    dao.user?.id?.value
)