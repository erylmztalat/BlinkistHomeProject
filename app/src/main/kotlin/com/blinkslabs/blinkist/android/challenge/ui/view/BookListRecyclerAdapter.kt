package com.blinkslabs.blinkist.android.challenge.ui.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blinkslabs.blinkist.android.challenge.R
import com.blinkslabs.blinkist.android.challenge.data.model.BookList

class BookListRecyclerAdapter(private val onBookClickListener: OnBookClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = ArrayList<BookList>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.book_list_layout, parent, false)
        return BookListViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val bookListHolder = holder as BookListViewHolder
        val bookList = items[position]

        bookListHolder.titleTextView.text = bookList.title

        val bookAdapter = BookRecyclerAdapter(onBookClickListener)
        bookListHolder.recyclerView.apply {
            layoutManager = LinearLayoutManager(bookListHolder.itemView.context, LinearLayoutManager.HORIZONTAL, false)
            adapter = bookAdapter
            setHasFixedSize(true)
        }

        bookAdapter.setItems(bookList.books)
    }

    override fun getItemCount() = items.size

    fun setItems(items: List<BookList>) {
        this.items.clear()
        this.items.addAll(items)
    }

    private class BookListViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var titleTextView: TextView = v.findViewById(R.id.book_list_title)
        var recyclerView: RecyclerView = v.findViewById(R.id.book_list_recycler_view)
    }
}
