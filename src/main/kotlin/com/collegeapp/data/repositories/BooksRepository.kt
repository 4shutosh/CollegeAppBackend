package com.collegeapp.data.repositories

import com.collegeapp.data.CollegeDatabase

interface BooksRepository {

    // return book id or the complete book data
    suspend fun insertBook(
        bookName: String,
        libraryBookNumber: Long,
        maximumDaysAllowed: Int
    ): String
}

class BooksRepositoryImpl constructor(
    private val collegeDatabase: CollegeDatabase
) : BooksRepository {


    override suspend fun insertBook(bookName: String, libraryBookNumber: Long, maximumDaysAllowed: Int): String {
        return collegeDatabase.insertBook(bookName, libraryBookNumber, maximumDaysAllowed).toString()
    }


}