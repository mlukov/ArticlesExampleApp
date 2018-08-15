package com.example.articles.domain.models

import com.google.gson.annotations.SerializedName

import java.io.Serializable
import java.util.Date

class ArticleDetailData : Serializable {

    var id: Int = 0
    var title: String? = null
    var subtitle: String? = null
    var date: Date? = null
}
