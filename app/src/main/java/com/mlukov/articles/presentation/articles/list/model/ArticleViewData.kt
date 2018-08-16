package com.mlukov.articles.presentation.articles.list.model

import java.util.Date
import java.io.Serializable

open class ArticleViewData( val id :Int?,
                       val title: String?,
                       val subtitle: String?,
                       val date: Date? ): Serializable {
}
