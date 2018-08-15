package com.mlukov.articles.mvp

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class BasePresenter : IBasePresenter {

    val compositeDisposable = CompositeDisposable()

    override fun add(disposable : Disposable) {

        compositeDisposable.add(disposable)
    }

    override fun dispose() {
        compositeDisposable.clear()
    }
}