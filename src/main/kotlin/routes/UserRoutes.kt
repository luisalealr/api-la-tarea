package com.example.routes

import com.example.service.UserService
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get

fun Route.usersRoutes(userService: UserService = UserService()){
    get("/api/users"){
        val users = userService.getAllUsers()
        call.respond(users) // Envía los datos como respuesta HTTP
    }

    get("/api/user/{id}"){
        val idParam = call.parameters["id"]

        if (idParam == null) {
            call.respond(HttpStatusCode.BadRequest, "ID no proporcionado")
            return@get
        }

        val user = userService.userById(idParam)

        if (user != null) {
            call.respond(user)
        } else {
            call.respond(HttpStatusCode.NotFound, "Usuario no encontrado")
        }
    }

    delete("/api/user/{id}") {
        val id = call.parameters["id"]

        if (id == null) {
            call.respond(HttpStatusCode.BadRequest, "ID no proporcionado")
            return@delete
        }

        val deleted = userService.deleteUser(id)

        if (deleted) {
            call.respond(HttpStatusCode.OK, "Usuario eliminado exitosamente")
        } else {
            call.respond(HttpStatusCode.NotFound, "Usuario no encontrado")
        }
    }

}
