package com.collegeapp.models

import kotlinx.serialization.Serializable

//    Binary JavaScript Object Notation

@Serializable
data class User(
    val userId: Long,
    val accessToken: String,
    val name: String,
    val email: String,
    val imageUrl: String
)