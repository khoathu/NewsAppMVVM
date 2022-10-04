package com.example.newsappmvvm.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.newsappmvvm.R
import com.example.newsappmvvm.viewmodels.NewsViewModel
import kotlinx.android.synthetic.main.activity_news.*
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.closestDI
import org.kodein.di.android.x.viewmodel.viewModel

class NewsActivity : AppCompatActivity(), DIAware {

    override val di: DI by closestDI()
    val viewModel: NewsViewModel by viewModel()

    //lateinit var viewModel: NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        //val newsRepository = NewsRepository(ArticleDatabase(this))
        //val viewModelProviderFactory =
        //    NewsViewModelProviderFactory(application, newsRepository)
        //viewModel = ViewModelProvider(this, viewModelProviderFactory).get(NewsViewModel::class.java)


        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.newsNavHostFragment) as NavHostFragment
        val navController = navHostFragment.navController
        bottomNavigationView.setupWithNavController(navController)
    }
}