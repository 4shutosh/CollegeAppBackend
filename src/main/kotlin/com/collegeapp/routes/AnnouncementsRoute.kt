package com.collegeapp.routes

import com.collegeapp.data.CollegeDatabase
import com.collegeapp.utils.Constants.EndPoints.ROUTE_ANNOUNCEMENTS
import io.ktor.server.application.*
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
}