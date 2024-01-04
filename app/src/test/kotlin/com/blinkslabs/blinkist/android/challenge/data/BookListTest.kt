package com.blinkslabs.blinkist.android.challenge.data

import com.blinkslabs.blinkist.android.challenge.data.model.Book
import com.blinkslabs.blinkist.android.challenge.data.model.BookList
import com.blinkslabs.blinkist.android.challenge.data.model.FilterType
import org.junit.Assert.assertEquals
import org.junit.Test
import org.threeten.bp.LocalDate

class BookListTest {

    private val book1 = Book(id = "1", name = "A Test Book", author = "Author A", publishDate = LocalDate.of(2023, 1, 1), "")
    private val book2 = Book(id = "2", name = "B Test Book", author = "Author B", publishDate = LocalDate.of(2023, 1, 15), "")
    private val book3 = Book(id = "3", name = "C Test Book", author = "Author C", publishDate = LocalDate.of(2023, 1, 8), "")

    @Test
    fun filterBookListByWeek() {
        val books = listOf(book1, book2, book3)
        val filteredBookLists = BookList.filterBookList(books, FilterType.WEEK)

        assertEquals(3, filteredBookLists.size)

        val firstBookList = filteredBookLists[0]
        assertEquals("2023-W3", firstBookList.title)
        assertEquals(listOf(book2), firstBookList.books)

        val secondBookList = filteredBookLists[1]
        assertEquals("2023-W2", secondBookList.title)
        assertEquals(listOf(book3), secondBookList.books)

        val thirdBookList = filteredBookLists[2]
        assertEquals("2023-W1", thirdBookList.title)
        assertEquals(listOf(book1), thirdBookList.books)
    }

    @Test
    fun filterBookListAlphabetically() {
        val books = listOf(book1, book2, book3)
        val filteredBookLists = BookList.filterBookList(books, FilterType.ALPHABETICALLY)

        assertEquals(3, filteredBookLists.size)

        val firstBookList = filteredBookLists[0]
        assertEquals("A", firstBookList.title)
        assertEquals(listOf(book1), firstBookList.books)

        val secondBookList = filteredBookLists[1]
        assertEquals("B", secondBookList.title)
        assertEquals(listOf(book2), secondBookList.books)

        val thirdBookList = filteredBookLists[2]
        assertEquals("C", thirdBookList.title)
        assertEquals(listOf(book3), thirdBookList.books)
    }
}
