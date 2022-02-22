package com.collegeapp.routes.books

import com.collegeapp.auth.JwtService
import com.collegeapp.data.repositories.LibraryRepository
import com.collegeapp.utils.Constants
import com.collegeapp.utils.Constants.EndPoints.ROUTE_LIBRARY
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.java.KoinJavaComponent

object LibraryRoute {

    fun Route.enableLibraryRoute(jwtData: JwtService.JwtData) {
        val libraryRepository: LibraryRepository by KoinJavaComponent.inject(LibraryRepository::class.java)

        post("/$ROUTE_LIBRARY") {

            val userId = call.parameters[Constants.USER_ID]

            // assuming the userId is valid : check the authentication part of JWT
            if (userId != null) {
                val books = libraryRepository.getUserBooks(userId)

                if (books != null) call.respond(
                    HttpStatusCode.OK, books
                ) else call.respond(
                    HttpStatusCode.OK, "No Books Found"
                )
                return@post
            }

        }
    }
}