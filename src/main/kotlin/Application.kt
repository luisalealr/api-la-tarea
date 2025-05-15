package com.example

import com.example.config.initializeFirebase
import com.example.routes.authRoutes
import com.example.service.UserService
import io.ktor.server.application.*
import io.ktor.server.routing.routing

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {

    val userService = UserService()

    initializeFirebase() // desde config/Firebase.kt
    configureDatabases()
    configureSerialization(userService)
    configureRouting()
}
