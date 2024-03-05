package com.wildantechnoart.newsapp.network

import com.wildantechnoart.newsapp.model.ArticlesModel
import com.wildantechnoart.newsapp.model.SourceModel
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface BaseApiService {

    @Headers("Accept: application/json")
    @GET("top-headlines/sources")
    suspend fun getSourceList(
        @Header("Authorization") token: String?,
        @Query("category") category: String?,
    ): SourceModel

    @Headers("Accept: application/json")
    @GET("everything")
    suspend fun getArticleList(
        @Header("Authorization") token: String?,
        @Query("sources") sources: String?,
        @Query("page") page: Int?,
        @Query("q") q: String?
    ): ArticlesModel
}