package com.example.routes

import com.example.config.verifyFirebaseToken
import com.example.model.SubjectRequest
import com.example.service.SubjectService
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route

fun Route.subjectRoutes( subjectService: SubjectService = SubjectService()){
    route("/api/subject") {

        get("/list/{idUser}") {
            val idParam = call.parameters["idUser"]
            val subjectsByUser = subjectService.findSubjectByUser(idParam )
            call.respond(HttpStatusCode.Found, subjectsByUser)
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
    }
}