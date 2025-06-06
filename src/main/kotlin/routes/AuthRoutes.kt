package com.example.routes

import com.example.db.UserDAO
import com.example.db.UserTable
import com.example.model.AuthRequest
import com.example.model.LoginRequest
import com.example.model.LoginResponse
import com.example.model.User
import com.example.service.UserService
import com.google.firebase.auth.FirebaseAuth
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

suspend fun <T> dbQuery(block: suspend () -> T): T =
    newSuspendedTransaction(Dispatchers.IO) { block() }

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

        userService.registerUserFromFirebase(request.token, request.name)
        call.respond(HttpStatusCode.Created, mapOf("message" to "Usuario registrado correctamente"))
    }

    post("/api/login") {
        val request = call.receive<LoginRequest>()

        try {
            // 1. Verificar el token con Firebase
            val decodedToken = FirebaseAuth.getInstance().verifyIdToken(request.token)
            val uid = decodedToken.uid
            val email = decodedToken.email ?: throw IllegalArgumentException("Email no encontrado en el token")

            // 2. Validar si el usuario existe en la base de datos
            val user = dbQuery {
                UserDAO.findById(uid)
            }

            if (user != null) {
                call.respond(HttpStatusCode.OK, LoginResponse(success = true, message = "Inicio de sesión exitoso", uid = uid, email = email))
            } else {
                call.respond(HttpStatusCode.NotFound, LoginResponse(success = false, message = "Usuario no registrado en la base de datos"))
            }

        } catch (e: Exception) {
            call.respond(HttpStatusCode.Unauthorized, LoginResponse(success = false, message = "Token inválido o error: ${e.message}"))
        }
    }


}
