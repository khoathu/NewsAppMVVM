package com.example.newsappmvvm.ui

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.newsappmvvm.R
import kotlinx.android.synthetic.main.activity_news.*

class NewsActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        bottomNavigationView.setupWithNavController(newsNavHostFragment.findNavController())
    }
}