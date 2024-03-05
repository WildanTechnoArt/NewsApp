package com.wildantechnoart.newsapp.repository

import com.wildantechnoart.newsapp.network.BaseApiService
import kotlinx.coroutines.flow.flow

class NewsRepository(private val baseApi: BaseApiService) {

    private val token = "4bb37421359f4f09a4ac02846d549bc9"

    fun getSourceList(category: String?) = flow {
        val response = baseApi.getSourceList("Bearer $token", category)
        emit(response)
    }

    fun getArticleList(page: Int?, sources: String?, search: String?) = flow {
        val response = baseApi.getArticleList("Bearer $token", sources, page, search)
        emit(response)
    }
}