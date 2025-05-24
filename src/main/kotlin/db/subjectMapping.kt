package com.example.db

import com.example.model.SubjectRequest
import com.example.model.SubjectResponse
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object SubjectTable : IntIdTable("\"Subject\""){
    val title = varchar("title", 50)
    val colorHexa = varchar("colorHexa", 50).nullable()
    val userId = optReference("userId", UserTable.id, onDelete = ReferenceOption.CASCADE )
    val scheduleBlockId = optReference("scheduleBlockId", ScheduleTable.id, onDelete = ReferenceOption.CASCADE)
}

class SubjectDAO(id : EntityID<Int>): IntEntity(id) {
    companion object : IntEntityClass<SubjectDAO>(SubjectTable)
    var title by SubjectTable.title
    var colorHexa by SubjectTable.colorHexa
    var user by UserDAO optionalReferencedOn SubjectTable.userId
    var schedule by ScheduleDAO optionalReferencedOn SubjectTable.scheduleBlockId
}

fun subjectResponseDao(dao: SubjectDAO) = SubjectResponse (
    dao.id.value,
    dao.title,
    dao.colorHexa,
    dao.user?.id?.value,
    dao.schedule?.id?.value
)

fun subjectRequestDao(dao: SubjectDAO) = SubjectRequest (
    dao.title,
    dao.colorHexa,
    dao.user?.id?.value,
    dao.schedule?.id?.value
)