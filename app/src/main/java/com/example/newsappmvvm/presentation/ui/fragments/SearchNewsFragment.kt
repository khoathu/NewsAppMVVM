package com.example.newsappmvvm.presentation.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.AbsListView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsappmvvm.R
import com.example.newsappmvvm.presentation.ui.NewsActivity
import com.example.newsappmvvm.presentation.ui.adapters.NewsAdapter
import com.example.newsappmvvm.presentation.viewmodels.NewsViewModel
import com.example.newsappmvvm.utils.Constants.Companion.QUERY_PAGE_SIZE
import com.example.newsappmvvm.utils.Constants.Companion.SEARCH_NEWS_TIME_DELAY
import com.example.newsappmvvm.utils.Utils
import kotlinx.android.synthetic.main.fragment_search_news.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchNewsFragment : Fragment(R.layout.fragment_search_news) {
    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter

    var isRefreshNewList = true

    val TAG = "SearchNewsFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).newsViewModel
        setupRecycleView()

        var job: Job? = null
        edSearch.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(SEARCH_NEWS_TIME_DELAY)
                editable?.let {
                    if (it.toString().isNotEmpty()) {
                        viewModel.resetSearchPage()
                        //state.value.searchNewsArticles = null
                        isRefreshNewList = true
                        viewModel.searchNews(it.toString())
                    }
                }
            }
        }

        newsAdapter.setOnClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_searchNewsFragment_to_articleNewsFragment,
                bundle
            )
        }

        srlRefreshSearchNews.setOnRefreshListener {
            if (Utils.hasInternetConnection(requireContext())) {
                val searchText = edSearch.text.toString()
                if (searchText.isNotEmpty()) {
                    viewModel.resetSearchPage()
                    isRefreshNewList = true
                    viewModel.searchNews(searchText)
                }
            }
            srlRefreshSearchNews.isRefreshing = false
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED)
            {
                viewModel.searchNewsState.collect { state ->
                    if (state.isLoading) {
                        showProgressBar()
                    } else {
                        hideProgressBar()
                    }

                    state.searchNewsArticles?.let { articles ->
                        val isEmptyList = articles.isNullOrEmpty() && isRefreshNewList
                        showEmptyList(isEmptyList)
                        newsAdapter.differ.submitList(articles?.toList() ?: emptyList())
                        val totalPages = state.searchNewsTotalResults / QUERY_PAGE_SIZE + 2
                        isLastPage = totalPages == state.searchNewsPage
                        if (isLastPage) {
                            rvSearchNews.setPadding(0, 0, 0, 0)
                        }
                    }

                    if (state.errorMessage.isNotBlank()) {
                        Toast.makeText(
                            activity,
                            "An error occurred: ${state.errorMessage}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }

        /*viewModel.searchNews.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        val isEmptyList = newsResponse.articles.isEmpty() && isRefreshNewList
                        showEmptyList(isEmptyList)

                        newsAdapter.differ.submitList(newsResponse.articles.toList())
                        val totalPages = newsResponse.totalResults / QUERY_PAGE_SIZE + 2
                        isLastPage = totalPages == viewModel.searchNewsPage
                        if (isLastPage) {
                            rvSearchNews.setPadding(0, 0, 0, 0)
                        }
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let {
                        Toast.makeText(activity, "An error occurred: $it", Toast.LENGTH_LONG)
                            .show()
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })*/
    }

    private fun setupRecycleView() {
        newsAdapter = NewsAdapter()
        rvSearchNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@SearchNewsFragment.onScrollListener)
        }
    }

    private fun showEmptyList(isEmpty: Boolean) {
        if (isEmpty) {
            rvSearchNews.visibility = View.GONE
            tvEmptyList.visibility = View.VISIBLE
        } else {
            rvSearchNews.visibility = View.VISIBLE
            tvEmptyList.visibility = View.GONE
        }
    }

    private fun showProgressBar() {
        paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    private fun hideProgressBar() {
        paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false
        srlRefreshSearchNews.isRefreshing = false
    }

    var isLastPage = false
    var isLoading = false
    var isScrolling = false

    private val onScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = rvSearchNews.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning
                    && isTotalMoreThanVisible && isScrolling
            if (shouldPaginate) {
                if (Utils.hasInternetConnection(requireContext())) {
                    if (edSearch?.text.toString().isNotEmpty()) {
                        isRefreshNewList = false
                        viewModel.searchNews(edSearch?.text.toString())
                        isScrolling = false
                    }
                }
            }
        }

    }
}