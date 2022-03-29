@file:Suppress("EXPERIMENTAL_IS_NOT_ENABLED")

package com.collegeapp.services

import com.collegeapp.data.CollegeDatabase
import com.collegeapp.utils.getDateFromTimeStamp
import io.ktor.server.application.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject
import kotlin.time.Duration.Companion
import kotlin.time.ExperimentalTime

/**
 * Library Service to add penalty to the users if they did not return the book before the [CollegeBookOwnerData::returnTimeStamp]
 * this method checks the date of server with returnTimeStamp of all the issued books from the entire database and then adds the penalty
 * runs once per day
 */
@OptIn(ExperimentalTime::class)
object LibraryService {

    private val collegeDatabase: CollegeDatabase by inject(CollegeDatabase::class.java)

    fun Application.startLibraryService() {
        launch(Dispatchers.IO) {
            while (true) {
                checkForReturnBookTask()
                delay(Companion.days(1))
//                delay(Companion.minutes(1))
            }
        }
    }

    private suspend fun checkForReturnBookTask() {
        println("LibraryService: checking for any book to return")
        // todo add try catch here
        val currentTimeStamp = System.currentTimeMillis()
        collegeDatabase.getAllBooks().data?.forEach { book ->
            // check the issued books
            if (!book.isAvailableToIssue && book.ownerData != null) {
                if (getDateFromTimeStamp(currentTimeStamp) > getDateFromTimeStamp(book.ownerData.returnTimeStamp)) {
                    // handle the penalty
                    collegeDatabase.addPenaltyToUserLibraryForBook(book.ownerData.userId, book.bookId)
                }
            }
        }
    }
}