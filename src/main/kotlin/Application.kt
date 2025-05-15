package com.example

import com.example.service.UserService
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {

    val userService = UserService()

    configureSerialization(userService)
    configureDatabases()
    configureRouting()
}
