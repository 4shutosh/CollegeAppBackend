package com.collegeapp.routes

import com.collegeapp.data.CollegeDatabase
import com.collegeapp.models.requests.InsertAnnouncementRoute
import com.collegeapp.utils.Constants.ANNOUNCEMENT_ID
import com.collegeapp.utils.Constants.EndPoints.ROUTE_ANNOUNCEMENTS
import com.collegeapp.utils.Constants.EndPoints.ROUTE_ANNOUNCEMENTS_INSERT
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.java.KoinJavaComponent.inject

object AnnouncementsRoute {

    private val collegeDatabase: CollegeDatabase by inject(CollegeDatabase::class.java)

    fun Route.getAllAnnouncements() {
        get("/$ROUTE_ANNOUNCEMENTS") {

            val response = collegeDatabase.getAllAnnouncements()

            call.respond(response)

            return@get
        }
    }

    fun Route.insertAnnouncements() {
        post("/$ROUTE_ANNOUNCEMENTS_INSERT") {
            val request = call.receive<InsertAnnouncementRoute>()

            val response = collegeDatabase.insertAnnouncement(request)

            call.respond(response)

            return@post
        }
    }

    fun Route.deleteAnnouncement() {
        post("/$ROUTE_ANNOUNCEMENTS_INSERT") {
            val request = call.parameters[ANNOUNCEMENT_ID]

            if (request != null) {
                val response = collegeDatabase.deleteAnnouncement(request)
                call.respond(response)
            } else {
                call.respond(HttpStatusCode.BadRequest)
            }
            return@post
        }
    }
}