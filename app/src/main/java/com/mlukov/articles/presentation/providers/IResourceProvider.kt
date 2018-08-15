package com.mlukov.articles.presentation.providers

import android.support.annotation.StringRes

interface IResourceProvider {

    fun getString(@StringRes stringResId: Int): String
}
