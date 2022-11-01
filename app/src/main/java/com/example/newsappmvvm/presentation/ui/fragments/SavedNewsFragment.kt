package com.example.newsappmvvm.presentation.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsappmvvm.R
import com.example.newsappmvvm.presentation.ui.NewsActivity
import com.example.newsappmvvm.presentation.ui.adapters.NewsAdapter
import com.example.newsappmvvm.presentation.viewmodels.NewsViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_saved_news.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SavedNewsFragment : Fragment(R.layout.fragment_saved_news) {
    lateinit var viewModel: NewsViewModel
    lateinit var favoriteNewsAdapter: NewsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).newsViewModel
        setupRecycleView()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED)
            {
                viewModel.getFavoriteArticles().collect { articles ->
                    favoriteNewsAdapter.differ.submitList(articles)
                }
            }
        }

        val onItemTouchHelper = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val article = favoriteNewsAdapter.differ.currentList[position]
                viewModel.deleteArticle(article)
                Snackbar.make(rvSavedNews, "Article Deleted successfully", Snackbar.LENGTH_LONG)
                    .apply {
                        setAction("Undo", View.OnClickListener {
                            viewModel.saveArticle(article)
                            lifecycleScope.launch {
                                delay(100)
                                rvSavedNews?.smoothScrollToPosition(position)
                            }
                        })
                        show()

                    }
            }
        }

        ItemTouchHelper(onItemTouchHelper).apply {
            attachToRecyclerView(rvSavedNews)
        }

        favoriteNewsAdapter.setOnClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_savedNewsFragment_to_articleNewsFragment,
                bundle
            )
        }

    }

    private fun setupRecycleView() {
        favoriteNewsAdapter = NewsAdapter()
        rvSavedNews.apply {
            adapter = favoriteNewsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}