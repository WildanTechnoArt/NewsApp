package com.wildantechnoart.newsapp.model

import com.google.gson.annotations.SerializedName

data class Response(
    @SerializedName("message") var message: String? = null
)