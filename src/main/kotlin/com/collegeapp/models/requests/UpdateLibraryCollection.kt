package com.collegeapp.models.requests

data class UpdateLibraryCollection(
    val userEmail: String,
    val penalty: Int
)