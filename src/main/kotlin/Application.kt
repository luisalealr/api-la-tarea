package com.example

import com.example.config.initializeFirebase
import com.example.routes.authRoutes
import com.example.service.UserService
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.routing
import java.io.FileInputStream

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {

    install(ContentNegotiation) {
        json()
    }
    configureDatabases()
    configureRouting()

    val userService = UserService()



    initializeFirebase()

    configureSerialization(userService)

}
