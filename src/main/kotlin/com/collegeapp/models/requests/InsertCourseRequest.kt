package com.collegeapp.models.requests

data class InsertCourseRequest(
    val courseName: String,
    val courseCode: String,
    val courseDescription: String,
    val courseFacultyName: String
)