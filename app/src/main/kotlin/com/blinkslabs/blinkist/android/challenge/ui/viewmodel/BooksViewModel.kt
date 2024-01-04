package com.blinkslabs.blinkist.android.challenge.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blinkslabs.blinkist.android.challenge.data.BooksService
import com.blinkslabs.blinkist.android.challenge.data.model.Book
import com.blinkslabs.blinkist.android.challenge.data.model.BookList
import com.blinkslabs.blinkist.android.challenge.data.model.FilterType
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class BooksViewModel @Inject constructor(private val booksService: BooksService) : ViewModel() {

    private val subscriptions = CompositeDisposable()

    private val bookLists = MutableLiveData<List<BookList>>()

    fun bookLists(): LiveData<List<BookList>> = bookLists

    private val _isThereNewBook = MutableLiveData<Boolean>()
    val isThereNewBook: MutableLiveData<Boolean> = _isThereNewBook

    fun fetchBooks(type: FilterType) {
        viewModelScope.launch() {
            try {
                bookLists.value = fetchBooksByFilter(booksService.getBooks(), type)
            } catch (e: Exception) {
                Timber.e(e, "while loading data")
            }
        }
    }

    fun refreshBooks(type: FilterType) {
        viewModelScope.launch {
            booksService.refreshBooks()
            bookLists.value = fetchBooksByFilter(booksService.getBooks(), type)
        }
    }

    fun isThereNewBook() {
        viewModelScope.launch {
            _isThereNewBook.postValue(booksService.isThereNewBook())
        }
    }

    fun fetchBooksByFilter(books: List<Book>, type: FilterType): List<BookList> {
        return BookList.filterBookList(books, type)
    }

    override fun onCleared() {
        subscriptions.clear()
    }
}