package com.blinkslabs.blinkist.android.challenge.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.blinkslabs.blinkist.android.challenge.CoroutinesTestRule
import com.blinkslabs.blinkist.android.challenge.data.BooksService
import com.blinkslabs.blinkist.android.challenge.data.model.Book
import com.blinkslabs.blinkist.android.challenge.data.model.FilterType
import com.blinkslabs.blinkist.android.challenge.ui.viewmodel.BooksViewModel
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class BooksViewModelShould {

    @get:Rule
    val liveDataRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutinesRule = CoroutinesTestRule()

    @Mock
    lateinit var booksService: BooksService

    @InjectMocks
    lateinit var viewModel: BooksViewModel

    private val mockBooks: List<Book> = listOf(mock(), mock(), mock())

    @Test
    fun callGetBooksOnServiceWhenFetchBooksIsCalled() = runTest {
        givenASuccessfulBooksServiceCall(mockBooks)
        viewModel.fetchBooks(FilterType.WEEK)
        verify(booksService).getBooks()
    }

    @Test
    fun showBooksOnViewWhenFetchBooksIsSuccessful() {
        givenASuccessfulBooksServiceCall(mockBooks)
        viewModel.fetchBooks(FilterType.WEEK)
        viewModel.bookLists().observeForever {
            assertEquals(mockBooks, it.map { list -> list.books })
        }
    }

    private fun givenASuccessfulBooksServiceCall(result: List<Book>) {
        runBlocking { whenever(booksService.getBooks()).thenReturn(result) }
    }
}
