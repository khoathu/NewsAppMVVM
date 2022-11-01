package com.example.newsappmvvm.presentation.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.newsappmvvm.R
import com.example.newsappmvvm.presentation.viewmodels.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_news.*
import javax.inject.Inject

@AndroidEntryPoint
class NewsActivity : AppCompatActivity() {

    //lateinit var viewModel: NewsViewModel
    val newsViewModel: NewsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        /*val newsRepository = NewsRepositoryImpl(ArticleDatabase(this).getArticleDao())
        val getBreakingNewsUseCase = GetBreakingNewsUseCase(newsRepository)
        val requestSearchNewsUseCase = RequestSearchNewsUseCase(newsRepository)
        val getSavedArticlesUseCase = GetSavedArticlesUseCase(newsRepository)
        val requestDeleteArticleUseCase = RequestDeleteArticleUseCase(newsRepository)
        val requestUpsertArticleUseCase = RequestUpsertArticleUseCase(newsRepository)

        val viewModelProviderFactory =
            NewsViewModelProviderFactory(
                application,
                getBreakingNewsUseCase,
                requestSearchNewsUseCase,
                getSavedArticlesUseCase,
                requestDeleteArticleUseCase,
                requestUpsertArticleUseCase
            )
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(NewsViewModel::class.java)
        */

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.newsNavHostFragment) as NavHostFragment
        val navController = navHostFragment.navController
        bottomNavigationView.setupWithNavController(navController)
    }
}