package com.collegeapp.data.repositories

import com.collegeapp.data.CollegeDatabase
import com.collegeapp.models.local.CollegeBook
import com.collegeapp.models.local.UserBookData

interface LibraryRepository {

    // todo return the confirmation data
    suspend fun issueTheBookForUser(userId: String, book: CollegeBook): Boolean

    suspend fun getUserBooks(userId : String) : List<UserBookData>?
}

class LibraryRepositoryImpl constructor(
    private val collegeDatabase: CollegeDatabase
) : LibraryRepository {

    override suspend fun issueTheBookForUser(userId: String, book: CollegeBook): Boolean {
        return false
    }

    override suspend fun getUserBooks(userId: String): List<UserBookData>? {
        return collegeDatabase.getAllUserBooks(userId)
    }

}