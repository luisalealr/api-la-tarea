package com.example.routes

import com.example.config.verifyFirebaseToken
import com.example.model.SubjectRequest
import com.example.service.SubjectService
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import kotlin.text.toIntOrNull

fun Route.subjectRoutes( subjectService: SubjectService = SubjectService()){
    route("/api/subject") {

        get("/list") {
            val authHeader = call.request.headers["Authorization"]
                ?: return@get call.respond(HttpStatusCode.Unauthorized)

            val token = authHeader.removePrefix("Bearer ").trim()
            val firebaseToken = verifyFirebaseToken(token)
                ?: return@get call.respond(HttpStatusCode.Unauthorized)

            val userId = firebaseToken.uid

            val subjects = subjectService.findSubjectByUser(userId)
            call.respond(subjects)
        }

        post("/create") {
            val authHeader = call.request.headers["Authorization"] ?: return@post call.respond(HttpStatusCode.Unauthorized)
            val token = authHeader.removePrefix("Bearer ").trim()
            val firebaseToken = verifyFirebaseToken(token)
                ?: return@post call.respond(HttpStatusCode.Unauthorized)

            val userId = firebaseToken.uid
            val subjectRequest = call.receive<SubjectRequest>()
            subjectService.createSubject(subjectRequest.copy(userId = userId))
            call.respond(HttpStatusCode.Created, "Ha sido creada la materia")
        }

        put("/update/{id}") {
            val idParam = call.parameters["id"]
            val id = idParam?.toIntOrNull()

            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "ID inválido")
                return@put
            }

            val subject = subjectService.subjectById(id)

            if(subject != null){
                val subjectUpdated = call.receive<SubjectRequest>()
                subjectService.updateSubject(id, subjectUpdated)
                call.respond(HttpStatusCode.OK, "Ha sido actualizada la materia")
            } else{
                call.respond(HttpStatusCode.NotFound, "Materia no encontrada")
            }
        }

        delete("/{id}") {
            val idParam = call.parameters["id"]
            val id = idParam?.toIntOrNull()

            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "ID no proporcionado")
                return@delete
            }

            val deleted = subjectService.deleteSubject(id)

            if (deleted) {
                call.respond(HttpStatusCode.OK, "Materia eliminada exitosamente")
            } else {
                call.respond(HttpStatusCode.NotFound, "Materia no encontrada")
            }
        }
    }
}