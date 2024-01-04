package com.blinkslabs.blinkist.android.challenge.ui.view

import com.blinkslabs.blinkist.android.challenge.data.model.Book

interface OnBookClickListener {
    fun onBookClick(book: Book)
}