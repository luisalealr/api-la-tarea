package com.example.routes

import com.example.config.verifyFirebaseToken
import com.example.model.SubjectRequest
import com.example.model.TaskRequest
import com.example.model.TaskUpdate
import com.example.service.TaskService
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import kotlin.text.toIntOrNull

fun Route.taskRoutes( taskService: TaskService = TaskService()){
    route("/api/task") {

        post("/create") {
            val authHeader = call.request.headers["Authorization"] ?: return@post call.respond(HttpStatusCode.Unauthorized)
            val token = authHeader.removePrefix("Bearer ").trim()
            val firebaseToken = verifyFirebaseToken(token)
                ?: return@post call.respond(HttpStatusCode.Unauthorized)

            val userId = firebaseToken.uid
            val taskRequest = call.receive<TaskRequest>()
            taskService.createTask(taskRequest.copy(userId = userId))
            call.respond(HttpStatusCode.Created, "Funciona mija :D")
        }

        get("/list"){
            val tasks = taskService.getAllTask()
            call.respond(tasks)
        }

        get("/{id}"){
            val idParam = call.parameters["id"]
            val id = idParam?.toIntOrNull()

            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "ID inválido")
                return@get
            }

            val task = taskService.taskById(id)

            if (task != null) {
                call.respond(task)
            } else {
                call.respond(HttpStatusCode.NotFound, "Tarea no encontrada")
            }
        }

        put("/update/{id}") {
            val idParam = call.parameters["id"]
            val id = idParam?.toIntOrNull()

            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "ID inválido")
                return@put
            }

            val subject = taskService.taskById(id)

            if(subject != null){
                val taskUpdated = call.receive<TaskUpdate>()
                taskService.updateTask(id, taskUpdated)
                call.respond(HttpStatusCode.OK, "Ha sido actualizada la tarea")
            } else{
                call.respond(HttpStatusCode.NotFound, "Tarea no encontrada")
            }


        }

    }
}