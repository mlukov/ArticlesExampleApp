package com.mlukov.articles.domain.models


import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.Date

open class ArticleData : Serializable{

    @SerializedName("id")
    var id: Int? = null

    @SerializedName("title")
    var title: String? = null

    @SerializedName("subtitle")
    var subtitle: String? = null

    @SerializedName("date")
    var date: Date? = null

}
