package com.collegeapp.models.requests

data class ReturnBookRequest(
    val libraryBookNumber: Long,
    val userEmail: String,
)