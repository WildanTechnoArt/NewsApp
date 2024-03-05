package com.wildantechnoart.newsapp.model

import com.google.gson.annotations.SerializedName

data class ArticlesModel(
    @SerializedName("status") var status: String? = null,
    @SerializedName("totalResults") var totalResults: Long? = null,
    @SerializedName("articles") var articles: ArrayList<Articles> = arrayListOf()
)

data class Articles(
    @SerializedName("author") var author: String? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("url") var url: String? = null,
    @SerializedName("urlToImage") var urlToImage: String? = null,
    @SerializedName("publishedAt") var publishedAt: String? = null,
    @SerializedName("content") var content: String? = null
)