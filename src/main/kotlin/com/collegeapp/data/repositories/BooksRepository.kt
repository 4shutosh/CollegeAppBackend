package com.collegeapp.data.repositories

import com.collegeapp.data.CollegeDatabase
import com.collegeapp.models.ServerResponse
import com.collegeapp.models.local.CollegeBook

interface BooksRepository {

    // return book id or the complete book data
    suspend fun insertBook(
        bookName: String,
        libraryBookNumber: Long,
        maximumDaysAllowed: Long
    ): String

    suspend fun getBookFromLibraryBookNumber(libraryBookNumber: Long) : ServerResponse<CollegeBook?>
}

class BooksRepositoryImpl constructor(
    private val collegeDatabase: CollegeDatabase
) : BooksRepository {


    override suspend fun insertBook(bookName: String, libraryBookNumber: Long, maximumDaysAllowed: Long): String {
        return collegeDatabase.insertBook(bookName, libraryBookNumber, maximumDaysAllowed)
    }

    override suspend fun getBookFromLibraryBookNumber(libraryBookNumber: Long): ServerResponse<CollegeBook?> {
        return collegeDatabase.getBookFromBookLibraryNumber(libraryBookNumber)
    }


}