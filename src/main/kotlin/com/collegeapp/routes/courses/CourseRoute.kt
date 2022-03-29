package com.collegeapp.routes.courses

import com.collegeapp.data.repositories.CourseRepository
import com.collegeapp.models.requests.InsertCourseRequest
import com.collegeapp.utils.Constants.EndPoints.ROUTE_COURSES
import com.collegeapp.utils.Constants.EndPoints.ROUTE_COURSES_INSERT
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.java.KoinJavaComponent.inject

object CourseRoute {

    private val courseRepository: CourseRepository by inject(CourseRepository::class.java)

    fun Route.getAllCourses() {
        get("/$ROUTE_COURSES") {

            val response = courseRepository.getAllCourses()

            call.respond(response)

            return@get
        }
    }

    fun Route.insertUpdateCourse() {
        post("/$ROUTE_COURSES_INSERT") {
            val insertRequest = call.receive<InsertCourseRequest>()

            val insertResponse = courseRepository.insertUpdateCourse(insertRequest)

            call.respond(insertResponse)

            return@post

        }
    }

}