package com.collegeapp.data.repositories

import com.collegeapp.data.CollegeDatabase
import com.collegeapp.models.ServerResponse

interface LibraryRepository {

    // todo return the confirmation data
    suspend fun issueTheBookForUser(userId: String, libraryBookNumber: Long): ServerResponse<Any>
    suspend fun returnTheBookForUser(userEmail: String, libraryBookNumber: Long): ServerResponse<Any>

    suspend fun getUserBooks(userId: String): ServerResponse<Any>

    suspend fun updateUserPenalty(userEmail: String, penalty: Int): ServerResponse<Any>
}

class LibraryRepositoryImpl constructor(
    private val collegeDatabase: CollegeDatabase
) : LibraryRepository {

    override suspend fun issueTheBookForUser(userId: String, libraryBookNumber: Long): ServerResponse<Any> {
        return collegeDatabase.checkForBookAndIssue(userId, libraryBookNumber)
    }

    override suspend fun returnTheBookForUser(userEmail: String, libraryBookNumber: Long): ServerResponse<Any> {
        return collegeDatabase.returnBook(userEmail, libraryBookNumber)
    }

    override suspend fun getUserBooks(userId: String): ServerResponse<Any> {
        return collegeDatabase.getAllUserBooks(userId)
    }

    override suspend fun updateUserPenalty(userEmail: String, penalty: Int): ServerResponse<Any> {
        return collegeDatabase.updateUserLibraryTotalPenalty(userEmail, penalty)
    }
}