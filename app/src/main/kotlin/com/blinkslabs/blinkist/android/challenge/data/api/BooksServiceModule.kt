package com.blinkslabs.blinkist.android.challenge.data.api

import android.content.Context
import androidx.room.Room
import com.blinkslabs.blinkist.android.challenge.data.BooksService
import dagger.Module
import dagger.Provides

@Module
class BooksServiceModule {

    @Provides
    fun provideBookDao(appDatabase: AppDatabase): BookDao = appDatabase.bookDao()

    @Provides
    fun provideAppDatabase(applicationContext: Context): AppDatabase =
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "book_database"
        ).build()

    @Provides
    fun provideBooksService(booksApi: BooksApi, bookDao: BookDao): BooksService {
        return BooksService(booksApi, bookDao)
    }
}
