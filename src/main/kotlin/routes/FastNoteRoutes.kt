package com.example.routes

import com.example.config.verifyFirebaseToken
import com.example.model.FastNoteRequest
import com.example.model.TaskRequest
import com.example.service.FastNoteService
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import kotlin.text.removePrefix
import kotlin.text.toIntOrNull

fun Route.fastNoteRoutes(fastNoteService: FastNoteService = FastNoteService()){
    route("/api/note") {

        post("/create") {
            val authHeader = call.request.headers["Authorization"] ?: return@post call.respond(HttpStatusCode.Unauthorized)
            val token = authHeader.removePrefix("Bearer ").trim()
            val firebaseToken = verifyFirebaseToken(token)
                ?: return@post call.respond(HttpStatusCode.Unauthorized)

            val userId = firebaseToken.uid
            val noteRequest = call.receive<FastNoteRequest>()
            fastNoteService.createNote(noteRequest.copy(userId = userId))
            call.respond(HttpStatusCode.Created, "Funciona mija :D")
        }

        get("/"){
            val tasks = fastNoteService.getAllNotes()
            call.respond(tasks)
        }

        get("/{id}"){
            val idParam = call.parameters["id"]
            val id = idParam?.toIntOrNull()

            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "ID inválido")
                return@get
            }

            val note = fastNoteService.noteById(id)

            if (note != null) {
                call.respond(note)
            } else {
                call.respond(HttpStatusCode.NotFound, "Tarea no encontrada")
            }
        }

        get("/list") {
            val authHeader = call.request.headers["Authorization"]
                ?: return@get call.respond(HttpStatusCode.Unauthorized)

            val token = authHeader.removePrefix("Bearer ").trim()
            val firebaseToken = verifyFirebaseToken(token)
                ?: return@get call.respond(HttpStatusCode.Unauthorized)

            val userId = firebaseToken.uid

            val notes = fastNoteService.getNotesByUser(userId)
            call.respond(notes)
        }

        delete("/{id}") {
            val idParam = call.parameters["id"]
            val id = idParam?.toIntOrNull()

            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "ID no proporcionado")
                return@delete
            }

            val deleted = fastNoteService.deleteNote(id)

            if (deleted) {
                call.respond(HttpStatusCode.OK, "Nota eliminada exitosamente")
            } else {
                call.respond(HttpStatusCode.NotFound, "Nota no encontrada")
            }
        }
    }
}