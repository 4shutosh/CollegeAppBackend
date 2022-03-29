package com.collegeapp.models.local

data class CollegeCourse(
    val id: Long,
    val name: String,
    val code: String,
    val description: String,
    val facultyName: String
)