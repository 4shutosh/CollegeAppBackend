package com.collegeapp.models

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId

//    Binary JavaScript Object Notation

@Serializable
data class CollegeUser(
    val userId: String,
    val accessToken: String,
    val name: String,
    val email: String,
    val imageUrl: String
)