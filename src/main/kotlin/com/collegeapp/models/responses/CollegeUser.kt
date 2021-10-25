package com.collegeapp.models.responses

import org.bson.codecs.pojo.annotations.BsonId

//    Binary JavaScript Object Notation

data class CollegeUser(
    @BsonId
    val userId: String,
    val accessToken: String,
    val name: String,
    val email: String,
    val imageUrl: String
)