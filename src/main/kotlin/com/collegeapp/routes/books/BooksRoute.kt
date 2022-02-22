package com.collegeapp.routes.books

import com.collegeapp.auth.JwtService
import com.collegeapp.data.repositories.BooksRepository
import com.collegeapp.models.requests.InsertBookRequest
import com.collegeapp.utils.Constants.BOOK_DAYS_ALLOWED
import com.collegeapp.utils.Constants.BOOK_NAME
import com.collegeapp.utils.Constants.EndPoints.INSERT_ROUTE_BOOKS
import com.collegeapp.utils.Constants.EndPoints.ROUTE_BOOKS
import com.collegeapp.utils.Constants.LIBRARY_NUMBER
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import org.koin.java.KoinJavaComponent.inject

object BooksRoute {


    // assuming the user data is valid here
    //

    fun Route.enableBooksRoute(jwtData: JwtService.JwtData) {

        val booksRepository: BooksRepository by inject(BooksRepository::class.java)

        post("/$INSERT_ROUTE_BOOKS") {


            val insertBookRequest = call.receive<InsertBookRequest>()

            val bookName = call.parameters[BOOK_NAME]
            val libraryBookNumber = call.parameters[LIBRARY_NUMBER]
            val maximumDaysAllowed = call.parameters[BOOK_DAYS_ALLOWED]

            // assuming the userId is valid : check the authentication part of JWT
            if (bookName != null && libraryBookNumber != null && maximumDaysAllowed != null) {
                val bookIdString = booksRepository.insertBook(bookName, libraryBookNumber.toLong(), maximumDaysAllowed.toInt())

            }
        }
    }
}


