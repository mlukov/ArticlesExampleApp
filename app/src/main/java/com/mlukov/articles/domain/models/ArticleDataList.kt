package com.mlukov.articles.domain.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ArticleDataList: Serializable {

    @SerializedName("articles")
    var articles: MutableList<ArticleData>? = null

    companion object {

        fun empty() : ArticleDataList {

            val emptyData = ArticleDataList()
            emptyData.articles = mutableListOf<ArticleData>()
            return emptyData
        }
    }

}