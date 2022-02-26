package com.collegeapp.models.requests

data class IssueBookRequest(
    val userId: String,
    val libraryBookNumber: Long
)