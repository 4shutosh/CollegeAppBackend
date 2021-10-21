package com.collegeapp.data.repositories

import com.collegeapp.auth.JwtService
import com.collegeapp.data.CollegeDatabase
import com.collegeapp.models.responses.CollegeUser

interface UserRepository {
    suspend fun login(
        jwtToken: JwtService.JwtData,
        userEmail: String,
        userName: String,
        userImageUrl: String
    ): CollegeUser
}

class UserRepositoryImpl constructor(
    private val collegeDatabase: CollegeDatabase
) : UserRepository {

    override suspend fun login(
        jwtToken: JwtService.JwtData,
        userEmail: String,
        userName: String,
        userImageUrl: String
    ): CollegeUser {
        return collegeDatabase.checkForUser(jwtToken, userEmail, userName, userImageUrl)
    }

}