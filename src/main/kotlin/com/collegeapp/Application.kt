package com.collegeapp

import com.collegeapp.di.koinModule
import com.collegeapp.plugins.configureAuthentication
import com.collegeapp.plugins.configureContentNegotiation
import com.collegeapp.plugins.configureRouting
import com.collegeapp.plugins.configureSecurity
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.koin.core.context.startKoin

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        CollegeApplication().apply { module() }
    }.start(wait = true)
}

fun main(args: Array<String>): Unit = EngineMain.main(args)

// this is for testing
fun Application.module() {
    CollegeApplication().apply { module() }
}

class CollegeApplication {

    fun Application.module() {

        startKoin {
            modules(koinModule)
        }

//        install(DefaultHeaders)
        install(CallLogging)
        configureContentNegotiation()
        configureSecurity()
        configureAuthentication()
        configureRouting()
    }
}
