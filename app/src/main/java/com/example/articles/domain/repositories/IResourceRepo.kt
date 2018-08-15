package com.example.articles.domain.repositories

import android.support.annotation.StringRes

interface IResourceRepo : IRepo {

    fun getString(@StringRes stringResId: Int): String
}
