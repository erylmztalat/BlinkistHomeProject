package com.blinkslabs.blinkist.android.challenge.ui.activity

import android.content.res.Configuration
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.blinkslabs.blinkist.android.challenge.BlinkistChallengeApplication
import com.blinkslabs.blinkist.android.challenge.R
import com.blinkslabs.blinkist.android.challenge.data.model.Book
import com.blinkslabs.blinkist.android.challenge.data.model.BookList
import com.blinkslabs.blinkist.android.challenge.data.model.FilterType
import com.blinkslabs.blinkist.android.challenge.ui.viewmodel.BooksViewModel
import com.blinkslabs.blinkist.android.challenge.ui.viewmodel.BooksViewModelFactory
import com.blinkslabs.blinkist.android.challenge.ui.view.BookListRecyclerAdapter
import com.blinkslabs.blinkist.android.challenge.ui.view.OnBookClickListener
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch
import javax.inject.Inject

class BooksActivity : AppCompatActivity(), OnBookClickListener {

    @Inject lateinit var booksViewModelFactory: BooksViewModelFactory

    private val viewModel by viewModels<BooksViewModel> { booksViewModelFactory }

    private lateinit var recyclerAdapter: BookListRecyclerAdapter

    private lateinit var refreshLinearLayout: LinearLayout
    private lateinit var spinner: Spinner
    private lateinit var recyclerView : RecyclerView
    private lateinit var swipeRefreshLayout : SwipeRefreshLayout

    private var bookPopup : MaterialDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_books)
        (application as BlinkistChallengeApplication).component.inject(this)
        supportActionBar?.hide()
        setupUI()
        setupObservers()
        fetchBooks(spinner.selectedItem.toString())
        checkNewBooks()
    }

    private fun setupUI() {
        spinner = findViewById(R.id.spinner)
        recyclerView = findViewById(R.id.recyclerView)
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        refreshLinearLayout = findViewById(R.id.refreshLinearLayout)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerAdapter = BookListRecyclerAdapter(this)
        recyclerView.adapter = recyclerAdapter

        ArrayAdapter.createFromResource(
            this,
            R.array.dropdown_items,
            R.layout.spinner_list
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.spinner_list)
            spinner.adapter = adapter
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = parent?.getItemAtPosition(position).toString()
                fetchBooks(selectedItem)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                print("Nothing Selected!")
            }
        }

        val isNightTheme = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK ==
                Configuration.UI_MODE_NIGHT_YES
        val backgroundColor = if (isNightTheme) R.color.blue else R.color.white
        val statusBarTint = if (isNightTheme) View.SYSTEM_UI_FLAG_VISIBLE else SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        swipeRefreshLayout.setBackgroundColor(ContextCompat.getColor(this, backgroundColor))
        window.statusBarColor = ContextCompat.getColor(this, backgroundColor)
        window.decorView.systemUiVisibility = statusBarTint

        swipeRefreshLayout.setOnRefreshListener { fetchBooks(spinner.selectedItem.toString()) }

        refreshLinearLayout.setOnClickListener {
            refreshLinearLayout.visibility = View.GONE
            refreshBooks(spinner.selectedItem.toString())
        }
    }

    private fun setupObservers() {
        viewModel.bookLists().observe(this, Observer { list ->
            showBooks(list)
            hideLoading()
        })

        viewModel.isThereNewBook.observe(this) {
            refreshLinearLayout.visibility = if (it) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (bookPopup != null) {
            bookPopup?.dismiss()
            bookPopup = null
        }
    }

    private fun showLoading() {
        swipeRefreshLayout.isRefreshing = true
    }

    private fun hideLoading() {
        swipeRefreshLayout.isRefreshing = false
    }

    private fun fetchBooks(selectedFilter: String) {
        showLoading()
        val filter = when (selectedFilter) {
            "Alphabetically" -> FilterType.ALPHABETICALLY
            "Weekly" -> FilterType.WEEK
            else -> FilterType.WEEK
        }
        viewModel.fetchBooks(filter)
    }

    private fun refreshBooks(selectedFilter: String) {
        showLoading()
        val filter = when (selectedFilter) {
            "Alphabetically" -> FilterType.ALPHABETICALLY
            "Weekly" -> FilterType.WEEK
            else -> FilterType.WEEK
        }
        viewModel.refreshBooks(filter)
    }

    private fun showBooks(bookList: List<BookList>) {
        recyclerAdapter.setItems(bookList)
        recyclerAdapter.notifyDataSetChanged()
        swipeRefreshLayout.isRefreshing = false
    }

    private fun showPopup(book:Book) {
        bookPopup = MaterialDialog(this)
            .customView(R.layout.book_detail_popup)
            .cancelOnTouchOutside(true)
            .cornerRadius(res = R.dimen.book_popup_radius)

        val view = bookPopup?.getCustomView()
        val dialogImageView = view?.findViewById<ImageView>(R.id.popupCoverImageView)
        val dialogTitleTextView = view?.findViewById<TextView>(R.id.popupTitleTextView)
        val dialogAuthorTextView = view?.findViewById<TextView>(R.id.popupAuthorTextView)

        dialogTitleTextView?.text = book.name
        dialogAuthorTextView?.text = book.author

        Picasso.get()
            .load(book.coverImageUrl)
            .into(dialogImageView)

        bookPopup?.show()
    }

    private fun checkNewBooks() {

        // Here websocket could use to check if there are new books
        // 5 seconds waiting to see refresh button. (New Books)
        lifecycleScope.launch {
            val countDownTimer = object : CountDownTimer(5000, 1000) {
                override fun onTick(p0: Long) {}

                override fun onFinish() {
                    viewModel.isThereNewBook()
                }
            }
            countDownTimer.start()
        }
    }

    override fun onBookClick(book: Book) {
        showPopup(book)
    }
}
