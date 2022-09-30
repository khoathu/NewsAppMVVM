package com.example.newsappmvvm.adapters

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsappmvvm.models.Article
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_article_preview.*

class ArticleViewHolder(private val context: Context, itemView: View) :
    RecyclerView.ViewHolder(itemView),
    LayoutContainer {
    override val containerView: View?
        get() = itemView

    fun bin(article: Article, onItemClickListener: ((article: Article) -> Unit)?) {
        apply {
            Glide.with(ivArticleImage.context.applicationContext).load(article?.urlToImage).into(ivArticleImage)
            tvSource.text = article?.source?.name
            tvTitle.text = article?.title
            tvDescription.text = article?.description
            tvPublishedAt.text = article?.publishedAt

            this.itemView.setOnClickListener {
                onItemClickListener?.invoke(article)
            }
        }
    }
}