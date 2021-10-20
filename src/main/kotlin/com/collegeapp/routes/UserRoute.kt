package com.collegeapp.routes

import com.collegeapp.auth.JwtService.JwtData
import com.collegeapp.data.CollegeDatabase.checkForUser
import com.collegeapp.utils.Constants
import com.collegeapp.utils.Constants.ROUTE_USER
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*

object UserRoute {

    fun Route.loginOrCreateUser(jwtData: JwtData) {
        post("/$ROUTE_USER") {

//            val request = try {
//                call.receive<LoginRequest>()
//            } catch (e: ContentTransformationException) {
//                call.respond(HttpStatusCode.BadRequest)
//                return@get
//            }

            val userEmail = call.parameters[Constants.EMAIL].toString()
            val userName = call.parameters[Constants.NAME].toString()
            val userImageUrl = call.parameters[Constants.IMAGE_URL].toString()

//            val userEmail = request.email
//            val userName = request.name
//            val userImageUrl = request.imageUrl

            if (userEmail.isEmpty() || userName.isEmpty()) {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }

            userEmail.let { email ->
                val loggedInUser = checkForUser(jwtData, email, userName, userImageUrl)

                call.respond(
                    HttpStatusCode.OK,
                    loggedInUser
                )
                return@post
            }
        }
    }

}