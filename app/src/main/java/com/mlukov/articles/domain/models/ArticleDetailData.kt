package com.mlukov.articles.domain.models

import com.google.gson.annotations.SerializedName

import java.io.Serializable
import java.util.Date

class ArticleDetailData :ArticleData(), Serializable {

    @SerializedName("body")
    var body: String? = null
}
