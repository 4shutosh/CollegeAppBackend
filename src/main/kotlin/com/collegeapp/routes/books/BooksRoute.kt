package com.collegeapp.routes.books

import com.collegeapp.auth.JwtService
import com.collegeapp.data.repositories.BooksRepository
import com.collegeapp.utils.Constants.EndPoints.ROUTE_BOOKS
import com.collegeapp.utils.Constants.USER_ID
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.java.KoinJavaComponent.inject

object BooksRoute {


    // assuming the user data is valid here
    //

    fun Route.enableBooksRoute(jwtData: JwtService.JwtData) {

        val userRepository: BooksRepository by inject(BooksRepository::class.java)

        post("/$ROUTE_BOOKS") {

            val userId = call.parameters[USER_ID]

            // assuming the userId is valid : check the authentication part of JWT
            if (userId != null) {
                val books = userRepository.getUserBooks(userId)

                if (books != null) call.respond(
                    HttpStatusCode.OK, books
                ) else   call.respond(
                    HttpStatusCode.OK, "No Books Found"
                )
                return@post
            }

        }

    }


}


