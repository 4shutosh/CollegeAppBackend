package com.collegeapp.models.requests

data class LoginRequest(
    val email: String,
    val name: String,
    val imageUrl: String
)