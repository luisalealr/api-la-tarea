package com.example.routes

import com.example.config.verifyFirebaseToken
import com.example.model.SubjectRequest
import com.example.service.ScheduleBlockService
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import kotlin.text.removePrefix
import kotlin.text.toIntOrNull

fun Route.scheduleBlockRoutes( scheduleBlockService: ScheduleBlockService = ScheduleBlockService()){
    route("/api/schedule") {


        delete("/{id}") {
            val idParam = call.parameters["id"]
            val id = idParam?.toIntOrNull()

            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "ID no proporcionado")
                return@delete
            }

            val deleted = scheduleBlockService.deleteScheduleBlock(id)

            if (deleted) {
                call.respond(HttpStatusCode.OK, "Materia eliminada exitosamente")
            } else {
                call.respond(HttpStatusCode.NotFound, "Materia no encontrada")
            }
        }

    }
}