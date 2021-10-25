package com.collegeapp.plugins

import com.collegeapp.auth.getJwtData
import com.collegeapp.routes.LoginRoute.loginOrCreateUser
import io.ktor.application.*
import io.ktor.http.content.*
import io.ktor.response.*
import io.ktor.routing.*

fun Application.configureRouting() {

    val jwtData = getJwtData()

    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        loginOrCreateUser(jwtData)
        // Static plugin. Try to access `/static/index.html`
        static("/static") {
            resources("static")
        }
    }
}
