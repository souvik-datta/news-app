package com.souvik.todayindia.model

import java.io.Serializable

data class Article(
    val status: String,
    val totalResult: Int,
    val articles: List<NewsData>
):Serializable
