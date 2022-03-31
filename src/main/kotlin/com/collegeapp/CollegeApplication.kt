package com.collegeapp

import com.collegeapp.di.koinModule
import com.collegeapp.plugins.configureAuthentication
import com.collegeapp.plugins.configureContentNegotiation
import com.collegeapp.plugins.configureRouting
import com.collegeapp.plugins.configureSecurity
import com.collegeapp.services.LibraryService.startLibraryService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import org.koin.core.context.GlobalContext.startKoin
import org.slf4j.event.Level


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
        install(CallLogging) {
            level = Level.INFO
            filter { call -> call.request.path().startsWith("/") }
        }
        install(CORS) {
            method(HttpMethod.Get)
            method(HttpMethod.Post)

            header(HttpHeaders.Authorization)
            header(HttpHeaders.ContentType)
            header(HttpHeaders.ContentLength)
            header(HttpHeaders.Connection)
            header(HttpHeaders.Accept)
            header(HttpHeaders.AcceptEncoding)
            header(HttpHeaders.Host)
            header(HttpHeaders.Vary)
            header(HttpHeaders.Server)
            header(HttpHeaders.Via)

            host("localhost:3000")
            host("iiit-nagpur-firebaseapp.com", schemes = listOf("https"))
            host("iiit-nagpur.web.com", schemes = listOf("https"))
        }

        configureContentNegotiation()
        configureSecurity()
        configureAuthentication()
        configureRouting()

        startLibraryService()
    }
}
