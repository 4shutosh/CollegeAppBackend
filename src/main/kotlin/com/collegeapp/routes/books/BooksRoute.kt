package com.collegeapp.routes.books

import com.collegeapp.data.repositories.BooksRepository
import com.collegeapp.models.ServerResponse
import com.collegeapp.models.requests.InsertBookRequest
import com.collegeapp.utils.Constants.EndPoints.ROUTE_BOOKS
import com.collegeapp.utils.Constants.EndPoints.ROUTE_INSERT_BOOKS
import com.collegeapp.utils.Constants.LIBRARY_BOOK_NUMBER
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.java.KoinJavaComponent.inject

object BooksRoute {

    private val booksRepository: BooksRepository by inject(BooksRepository::class.java)

    // assuming the user data is valid here

    fun Route.insertBooksRoute() {
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
                ServerResponse(
                    data = bookIdString,
                    status = HttpStatusCode.OK.value,
                    message = "Book Insert Success"
                )
            )
            return@post
        }
    }

    fun Route.getBookByLibraryRoute() {
        get("/$ROUTE_BOOKS") {

            val libraryBookNumber = call.parameters[LIBRARY_BOOK_NUMBER]

            println("GET Book Request found $libraryBookNumber")

            // assuming the userId is valid : check the authentication part of JWT
            if (libraryBookNumber != null) {
                val bookIdString = booksRepository.getBookFromLibraryBookNumber(libraryBookNumber.toLong())
                call.respond(
                    bookIdString
                )
                return@get
            }
        }
    }

    fun Route.getAllBooks() {
        get("/$ROUTE_BOOKS") {
            val allBooksResponse = booksRepository.getAllBooks()
            call.respond(
                allBooksResponse
            )
            return@get
        }
    }

}


