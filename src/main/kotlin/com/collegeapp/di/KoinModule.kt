package com.collegeapp.di

import com.collegeapp.data.CollegeDatabase
import com.collegeapp.data.repositories.BooksRepository
import com.collegeapp.data.repositories.BooksRepositoryImpl
import com.collegeapp.data.repositories.UserRepository
import com.collegeapp.data.repositories.UserRepositoryImpl
import org.koin.dsl.module

val koinModule = module {
    single { provideCollegeDatabase() }
    single { provideUserRepository(get() as CollegeDatabase) }
    single { provideBooksRepository(get() as CollegeDatabase) }
}

private fun provideCollegeDatabase(): CollegeDatabase = CollegeDatabase()

private fun provideUserRepository(
    collegeDatabase: CollegeDatabase
): UserRepository = UserRepositoryImpl(collegeDatabase)

private fun provideBooksRepository(
    collegeDatabase: CollegeDatabase
): BooksRepository = BooksRepositoryImpl(collegeDatabase)