package com.collegeapp.models.local

import org.bson.codecs.pojo.annotations.BsonId

data class CollegeBook(
    @BsonId
    val bookId: String,
    val libraryBookNumber: Long,
    var bookName: String,
    val maximumDaysAllowed: Long,
    val isAvailableToIssue: Boolean,
    val ownerData: CollegeBookOwnerData? = null
)

data class CollegeBookOwnerData(
    val userId: String,
    val email: String,
    val returnTimeStamp: Long,
)

data class UserLibraryData(
    @BsonId
    val id: String, // this id is equal to userEmail
    val userBookDataList: List<UserBookData>,
    val totalPenalty: Int = 0
)


data class UserBookData(
    val book: CollegeBook,
    val issueTimeStamp: Long,
    val returnTimeStamp: Long,
    var penalty: Int = 0
)
