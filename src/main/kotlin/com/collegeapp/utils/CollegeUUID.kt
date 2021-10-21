package com.collegeapp.utils


fun generateUserUid(length: Int = 10): String {
    val charPool = ('A'..'Z') + ('a'..'z') + ('0'..'9')
    return (1..length).map {
        charPool.random()
    }.joinToString("")
}