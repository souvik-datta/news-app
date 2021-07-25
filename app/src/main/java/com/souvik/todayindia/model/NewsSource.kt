package com.souvik.todayindia.model

import java.io.Serializable

data class NewsSource(
    val sources: List<SourceX>,
    val status: String
):Serializable