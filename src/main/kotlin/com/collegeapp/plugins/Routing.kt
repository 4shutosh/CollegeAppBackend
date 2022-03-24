package com.collegeapp.plugins

import com.collegeapp.auth.getJwtData
import com.collegeapp.routes.LoginRoute.loginOrCreateUser
import com.collegeapp.routes.books.BooksRoute.getAllBooks
import com.collegeapp.routes.books.BooksRoute.getBookByLibraryRoute
import com.collegeapp.routes.books.BooksRoute.insertOrUpdateBook
import com.collegeapp.routes.books.LibraryRoute.enableLibraryRoute
import com.collegeapp.routes.books.LibraryRoute.issueABookLibrary
import com.collegeapp.routes.books.LibraryRoute.returnAnIssuedBookRoute
import com.collegeapp.utils.Constants.JWT_SUBJECT
import io.ktor.server.application.*
import io.ktor.server.auth.*
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

        authenticate(JWT_SUBJECT) {

            // books
            getBookByLibraryRoute()
            insertOrUpdateBook()
            getAllBooks()

            // library
            enableLibraryRoute()
            issueABookLibrary()
            returnAnIssuedBookRoute()


            // Static plugin. Try to access `/static/index.html`
            static("/static") {
                resources("static")
            }
        }
    }
}
