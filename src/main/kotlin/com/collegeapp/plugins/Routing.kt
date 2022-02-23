package com.collegeapp.plugins

import com.collegeapp.auth.getJwtData
import com.collegeapp.routes.LoginRoute.loginOrCreateUser
import com.collegeapp.routes.books.BooksRoute.getBookByLibraryRoute
import com.collegeapp.routes.books.BooksRoute.insertBooksRoute
import com.collegeapp.routes.books.LibraryRoute.enableLibraryRoute
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {

    val jwtData = getJwtData()

    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        loginOrCreateUser(jwtData)

        // books
        getBookByLibraryRoute(jwtData)
        insertBooksRoute(jwtData)

        // library
        enableLibraryRoute(jwtData)


        // Static plugin. Try to access `/static/index.html`
        static("/static") {
            resources("static")
        }
    }
}
