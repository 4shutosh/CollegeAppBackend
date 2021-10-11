package com.collegeapp.plugins

import com.collegeapp.routes.UserRoute.loginOrCreateUser
import io.ktor.application.*
import io.ktor.http.content.*
import io.ktor.response.*
import io.ktor.routing.*

fun Application.configureRouting() {
    routing {
        get("/") {
                call.respondText("Hello World!")
            }

        loginOrCreateUser()
        // Static plugin. Try to access `/static/index.html`
        static("/static") {
            resources("static")
        }
    }
}
