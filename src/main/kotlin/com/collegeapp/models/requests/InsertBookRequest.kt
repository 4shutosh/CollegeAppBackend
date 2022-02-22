package com.collegeapp.models.requests

data class InsertBookRequest(
    val bookName: String,
    val libraryBookNumber: Long,
    val daysAllowed: Int
)