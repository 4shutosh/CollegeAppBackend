package com.collegeapp.routes.books

import com.collegeapp.auth.JwtService
import com.collegeapp.data.repositories.BooksRepository
import com.collegeapp.models.requests.InsertBookRequest
import com.collegeapp.utils.Constants.EndPoints.ROUTE_INSERT_BOOKS
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.java.KoinJavaComponent.inject

object BooksRoute {


    // assuming the user data is valid here

    fun Route.enableBooksRoute(jwtData: JwtService.JwtData) {

        val booksRepository: BooksRepository by inject(BooksRepository::class.java)

        post("/$ROUTE_INSERT_BOOKS") {

            val insertBookRequest = call.receive<InsertBookRequest>()

            println("Insert Book Request found $insertBookRequest")

            // assuming the userId is valid : check the authentication part of JWT
            val bookIdString = booksRepository.insertBook(
                insertBookRequest.bookName,
                insertBookRequest.libraryBookNumber,
                insertBookRequest.daysAllowed
            )

            call.respond(
                HttpStatusCode.OK,
                bookIdString
            )
            return@post
        }
    }
}


