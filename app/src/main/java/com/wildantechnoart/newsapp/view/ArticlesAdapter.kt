package com.wildantechnoart.newsapp.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wildantechnoart.newsapp.R
import com.wildantechnoart.newsapp.databinding.ItemArticlesBinding
import com.wildantechnoart.newsapp.model.Articles
import com.wildantechnoart.newsapp.utils.Constant

class ArticlesAdapter :
    ListAdapter<Articles, ArticlesAdapter.Holder>(MyDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            ItemArticlesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val data = getItem(position)
        val context = holder.itemView.context

        with(holder.binding) {
            Glide.with(context)
                .load(data.urlToImage)
                .into(imageArticles)

            textTitle.text = data.title ?: "-"
            textAuthor.text = "Author: ${data.author} (${data.publishedAt?.take(4)})"
            textDescription.text = data.description ?: "-"

            cardItem.setOnClickListener {
                toDetailFragment(it.context, data.url.toString())
            }
            btnDetail.setOnClickListener {
                toDetailFragment(it.context, data.url)
            }
        }
    }

    private fun toDetailFragment(context: Context, url: String?) {
        if (url != null) {
            val bundle = Bundle()
            bundle.putString(Constant.ARTICLE_URL, url)

            val intent = Intent(context, ArticleViewActivity::class.java)
            intent.putExtras(bundle)
            context.startActivity(intent)
        } else {
            Toast.makeText(
                context,
                context.getString(R.string.message_if_news_not_found), Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    class MyDiffCallback : DiffUtil.ItemCallback<Articles>() {
        override fun areItemsTheSame(oldItem: Articles, newItem: Articles): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Articles, newItem: Articles): Boolean {
            return oldItem == newItem
        }
    }

    class Holder(val binding: ItemArticlesBinding) : RecyclerView.ViewHolder(binding.root)
}