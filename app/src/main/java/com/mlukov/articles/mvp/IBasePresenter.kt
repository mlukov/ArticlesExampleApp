package com.mlukov.articles.mvp

import io.reactivex.disposables.Disposable

interface IBasePresenter {

    fun dispose()

    fun add(disposable : Disposable)
}