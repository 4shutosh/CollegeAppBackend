package com.collegeapp.di

import com.collegeapp.data.CollegeDatabase
import com.collegeapp.data.repositories.*
import org.koin.dsl.module

val koinModule = module {
    single { provideCollegeDatabase() }
    single { provideUserRepository(get() as CollegeDatabase) }
    single { provideBooksRepository(get() as CollegeDatabase) }
    single { provideLibraryRepository(get() as CollegeDatabase) }
    single { provideCourseRepository(get() as CollegeDatabase) }
}

private fun provideCollegeDatabase(): CollegeDatabase = CollegeDatabase()

private fun provideUserRepository(
    collegeDatabase: CollegeDatabase
): UserRepository = UserRepositoryImpl(collegeDatabase)

private fun provideBooksRepository(
    collegeDatabase: CollegeDatabase
): BooksRepository = BooksRepositoryImpl(collegeDatabase)

private fun provideLibraryRepository(
    collegeDatabase: CollegeDatabase
): LibraryRepository = LibraryRepositoryImpl(collegeDatabase)

private fun provideCourseRepository(
    collegeDatabase: CollegeDatabase
): CourseRepository = CourseRepositoryImpl(collegeDatabase)