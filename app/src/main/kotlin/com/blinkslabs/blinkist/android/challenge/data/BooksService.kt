package com.blinkslabs.blinkist.android.challenge.data

import com.blinkslabs.blinkist.android.challenge.data.api.BookDao
import com.blinkslabs.blinkist.android.challenge.data.api.BooksApi
import com.blinkslabs.blinkist.android.challenge.data.model.Book
import javax.inject.Inject

class BooksService @Inject constructor(
    private val booksApi: BooksApi, private val bookDao: BookDao
) {
    suspend fun getBooks(): List<Book> {
        val localBooks = bookDao.getAllBooks()
        return if (localBooks.isNotEmpty()) {
            localBooks
        } else {
            val apiBooks = booksApi.getAllBooks()
            bookDao.insertBooks(apiBooks)
            apiBooks
        }
    }

    suspend fun refreshBooks() {
        val apiBooks = booksApi.getAllBooks()
        bookDao.deleteAllBooks()
        bookDao.insertBooks(apiBooks)
    }

    suspend fun isThereNewBook(): Boolean {
        val localBooks = bookDao.getAllBooks()
        val apiBooks = booksApi.getAllBooks()
        return localBooks != apiBooks
    }
}
