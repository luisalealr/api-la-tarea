package com.example.db

import com.example.model.ScheduleBlockResponse
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object ScheduleTable : IntIdTable("ScheduleBlock"){
    val day = varchar("title", 50)
    val startHour = varchar("startHour", 50)
    val finishHour = varchar("finishHour", 50)
}

class ScheduleDAO (id : EntityID<Int>): IntEntity(id) {
    companion object : IntEntityClass<ScheduleDAO>(ScheduleTable)
    var day by ScheduleTable.day
    var startHour by ScheduleTable.startHour
    var finishHour by ScheduleTable.finishHour
}

fun scheduleDao(dao : ScheduleDAO) = ScheduleBlockResponse (
    dao.id.value,
    dao.day,
    dao.startHour,
    dao.finishHour
)