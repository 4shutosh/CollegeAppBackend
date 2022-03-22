package com.collegeapp.data.repositories

import com.collegeapp.data.CollegeDatabase
import com.collegeapp.models.ServerResponse

interface BooksRepository {

    // return book id or the complete book data
    suspend fun insertBook(
        bookName: String,
        libraryBookNumber: Long,
        maximumDaysAllowed: Long
    ): ServerResponse<String>

    suspend fun getBookFromLibraryBookNumber(libraryBookNumber: Long) : ServerResponse<Any?>
}

class BooksRepositoryImpl constructor(
    private val collegeDatabase: CollegeDatabase
) : BooksRepository {


    override suspend fun insertBook(bookName: String, libraryBookNumber: Long, maximumDaysAllowed: Long): ServerResponse<String> {
        return collegeDatabase.insertBook(bookName, libraryBookNumber, maximumDaysAllowed)
    }

    override suspend fun getBookFromLibraryBookNumber(libraryBookNumber: Long): ServerResponse<Any?> {
        return collegeDatabase.getBookFromBookLibraryNumber(libraryBookNumber)
    }


}