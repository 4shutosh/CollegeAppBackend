package com.collegeapp.routes.books

import com.collegeapp.auth.JwtService
import com.collegeapp.data.repositories.LibraryRepository
import com.collegeapp.models.requests.IssueBookRequest
import com.collegeapp.utils.Constants
import com.collegeapp.utils.Constants.EndPoints.LIBRARY_ISSUE
import com.collegeapp.utils.Constants.EndPoints.ROUTE_LIBRARY
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.java.KoinJavaComponent

object LibraryRoute {

    private val libraryRepository: LibraryRepository by KoinJavaComponent.inject(LibraryRepository::class.java)

    fun Route.issueABookLibrary(jwtData: JwtService.JwtData) {

        post("/$LIBRARY_ISSUE") {

//            val userId = call.parameters[Constants.USER_ID]

            val issueBookRequest = call.receive<IssueBookRequest>()

//             assuming the userId is valid : check the authentication part of JWT
            val bookIssueResponse =
                libraryRepository.issueTheBookForUser(issueBookRequest.userId, issueBookRequest.libraryBookNumber)

            call.respond(bookIssueResponse)
            return@post

        }
    }

    fun Route.enableLibraryRoute(jwtData: JwtService.JwtData) {

        get("/$ROUTE_LIBRARY") {
            val userId = call.parameters[Constants.USER_ID]

//             assuming the userId is valid : check the authentication part of JWT
            if (userId != null) {
                val bookIssueResponse = libraryRepository.getUserBooks(userId.toString())
                call.respond(bookIssueResponse)
            } else call.respond(HttpStatusCode.BadRequest)

            return@get

        }
    }
}