package com.example.articles.repositories

import android.content.Context

import com.example.articles.domain.repositories.IResourceRepo

class ResourceRepo(private val context: Context?) : IResourceRepo {


    override fun getString(stringResId: Int): String {

        return context?.getString(stringResId) ?: ""
    }
}
