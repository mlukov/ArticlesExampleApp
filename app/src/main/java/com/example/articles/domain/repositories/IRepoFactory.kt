package com.example.articles.domain.repositories


interface IRepoFactory {

    fun <T : IRepo> getRepository(classOfT: Class<T>): T
}
