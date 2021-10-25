package com.collegeapp.plugins

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*


fun Application.configureContentNegotiation() {

    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
            disableHtmlEscaping()
        }
    }
}