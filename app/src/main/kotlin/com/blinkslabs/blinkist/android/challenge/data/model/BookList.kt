package com.blinkslabs.blinkist.android.challenge.data.model

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

enum class FilterType {
    WEEK,
    ALPHABETICALLY
}

data class BookList(
    val title: String,
    val books: List<Book>
) {
    companion object {


        fun filterBookList(books: List<Book>, filterType: FilterType): List<BookList> {
            return when (filterType) {
                FilterType.WEEK -> filterByWeek(books)
                FilterType.ALPHABETICALLY -> filterAlphabetically(books)
            }
        }

        private fun filterByWeek(books: List<Book>): List<BookList> {
            val bookLists = mutableListOf<BookList>()
            val groupedBooks = books.sortedByDescending { it.publishDate }.groupBy { book ->
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val date = dateFormat.parse(book.publishDate.toString())
                val calendar = Calendar.getInstance()
                calendar.time = date
                "${calendar.get(Calendar.YEAR)}-W${calendar.get(Calendar.WEEK_OF_YEAR)}"
            }

            for ((week, booksInWeek) in groupedBooks) {
                bookLists.add(BookList(week, booksInWeek))
            }

            return bookLists
        }

        private fun filterAlphabetically(books: List<Book>): List<BookList> {
            val bookLists = mutableListOf<BookList>()
            val groupedBooks = books.groupBy { it.name[0].toUpperCase() }

            for ((letter, booksWithLetter) in groupedBooks) {
                val title = letter.toString()
                bookLists.add(BookList(title, booksWithLetter))
            }

            return bookLists.sortedBy { it.title }
        }
    }
}

