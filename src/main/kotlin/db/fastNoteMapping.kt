package com.example.db

import com.example.model.FastNoteResponse
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object NoteTable : IntIdTable("\"FastNote\"") {
    val title = varchar("title", 50)
    val description = text("description")
    val userId = optReference("userId", UserTable.id, onDelete = ReferenceOption.CASCADE )
}

class NoteDAO(id : EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<NoteDAO>(NoteTable)

    var title by NoteTable.title
    var description by NoteTable.description
    var user by UserDAO optionalReferencedOn NoteTable.userId
}

fun fastNoteDao(dao : NoteDAO) = FastNoteResponse (
    dao.id.value,
    dao.title,
    dao.description,
    dao.user?.id?.value
)