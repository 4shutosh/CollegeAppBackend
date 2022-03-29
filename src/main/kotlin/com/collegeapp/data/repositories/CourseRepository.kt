package com.collegeapp.data.repositories

import com.collegeapp.data.CollegeDatabase
import com.collegeapp.models.ServerResponse
import com.collegeapp.models.local.CollegeCourse
import com.collegeapp.models.requests.InsertCourseRequest

interface CourseRepository {

    suspend fun insertUpdateCourse(request: InsertCourseRequest): ServerResponse<Any>

    suspend fun getAllCourses(): ServerResponse<List<CollegeCourse>>
}

class CourseRepositoryImpl constructor(
    private val collegeDatabase: CollegeDatabase
) : CourseRepository {
    override suspend fun getAllCourses(): ServerResponse<List<CollegeCourse>> {
        return collegeDatabase.getAllCourses()
    }

    override suspend fun insertUpdateCourse(request: InsertCourseRequest): ServerResponse<Any> {
        return collegeDatabase.insertOrUpdateCourse(request)
    }

}