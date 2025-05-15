package com.example.routes

import com.example.model.AuthRequest
import com.example.model.User
import com.example.service.UserService
import com.google.firebase.auth.FirebaseAuth
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post


fun Route.authRoutes(userService: UserService = UserService()) {
    post ("/api/register") {
        val request = call.receive<AuthRequest>()

        val decodedToken = FirebaseAuth.getInstance().verifyIdToken(request.token)
        val uid = decodedToken.uid
        val email = decodedToken.email ?: return@post call.respond(HttpStatusCode.BadRequest)

        val user = User(
            id = uid,
            name = request.name,
            email = email,
            photo = decodedToken.picture
        )

        userService.createUser(user)
        call.respond(HttpStatusCode.Created, mapOf("message" to "Usuario registrado correctamente"))
    }
}
