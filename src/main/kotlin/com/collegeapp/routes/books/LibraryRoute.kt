package com.collegeapp.routes.books

import com.collegeapp.data.repositories.LibraryRepository
import com.collegeapp.models.requests.IssueBookRequest
import com.collegeapp.models.requests.ReturnBookRequest
import com.collegeapp.models.requests.UpdateLibraryCollection
import com.collegeapp.utils.Constants
import com.collegeapp.utils.Constants.EndPoints.LIBRARY_ISSUE
import com.collegeapp.utils.Constants.EndPoints.LIBRARY_PENALTY
import com.collegeapp.utils.Constants.EndPoints.LIBRARY_RETURN
import com.collegeapp.utils.Constants.EndPoints.ROUTE_LIBRARY
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.java.KoinJavaComponent

object LibraryRoute {

    private val libraryRepository: LibraryRepository by KoinJavaComponent.inject(LibraryRepository::class.java)

    fun Route.issueABookLibrary() {

        post("/$LIBRARY_ISSUE") {
            val issueBookRequest = call.receive<IssueBookRequest>()

            val bookIssueResponse =
                libraryRepository.issueTheBookForUser(issueBookRequest.userId, issueBookRequest.libraryBookNumber)

            call.respond(bookIssueResponse)
            return@post

        }
    }

    fun Route.enableLibraryRoute() {

        get("/$ROUTE_LIBRARY") {
            val userId = call.parameters[Constants.USER_ID]

            if (userId != null) {
                val bookIssueResponse = libraryRepository.getUserBooks(userId.toString())
                call.respond(bookIssueResponse)
            } else call.respond(HttpStatusCode.BadRequest)

            return@get

        }
    }

    fun Route.returnAnIssuedBookRoute() {
        post("/$LIBRARY_RETURN") {
            val returnBookRequest = call.receive<ReturnBookRequest>()
            val returnBookResponse =
                libraryRepository.returnTheBookForUser(returnBookRequest.userEmail, returnBookRequest.libraryBookNumber)

            call.respond(returnBookResponse)

            return@post
        }
    }

    fun Route.updateUserPenalty() {
        post("/$LIBRARY_PENALTY") {
            val updatePenaltyRequest = call.receive<UpdateLibraryCollection>()
            val updatePenaltyResponse =
                libraryRepository.updateUserPenalty(updatePenaltyRequest.userEmail, updatePenaltyRequest.penalty)

            call.respond(updatePenaltyResponse)

            return@post
        }
    }

}