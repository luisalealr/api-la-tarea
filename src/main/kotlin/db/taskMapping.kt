package com.example.db

import com.example.model.TaskRequest
import com.example.model.TaskResponse
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.javatime.date

object TaskTable : IntIdTable("\"Task\"") {
    val title = varchar("title", 50)
    val description = text("description")
    val createdAt = date("createdAt")
    val expiresAt = date("expiresAt").nullable()
    val colorHexa = varchar("colorHexa", 30).nullable()
    val priority = integer("priority").nullable()
    val userId = optReference("userId", UserTable.id, onDelete = ReferenceOption.CASCADE)
    val subjectId = optReference("subjectId", SubjectTable.id, onDelete = ReferenceOption.CASCADE)
}

class TaskDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<TaskDAO>(TaskTable)

    var title by TaskTable.title
    var description by TaskTable.description
    var createdAt by TaskTable.createdAt
    var expiresAt by TaskTable.expiresAt
    var colorHexa by TaskTable.colorHexa
    var priority by TaskTable.priority
    var user by UserDAO optionalReferencedOn TaskTable.userId
    var subject by SubjectDAO optionalReferencedOn TaskTable.subjectId
}

fun taskResponseDao(dao: TaskDAO) = TaskResponse (
    dao.id.value,
    dao.title,
    dao.description,
    dao.createdAt.toString(),
    dao.expiresAt?.toString(),
    dao.colorHexa,
    dao.priority,
    dao.user?.id?.value,
    dao.subject?.id?.value
)

fun taskRequestDao(dao: TaskDAO) = TaskRequest(

    dao.title,
    dao.description,
    dao.createdAt.toString(),
    dao.expiresAt?.toString(),
    dao.colorHexa,
    dao.priority,
    dao.user?.id?.value,
    dao.subject?.id?.value
)