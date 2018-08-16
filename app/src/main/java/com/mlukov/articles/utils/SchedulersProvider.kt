package com.mlukov.articles.utils

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

import javax.inject.Inject

class SchedulersProvider @Inject constructor(): ISchedulersProvider {

    override fun ioScheduler() : Scheduler {

        return Schedulers.io()
    }

    override fun uiScheduler() : Scheduler {

        return AndroidSchedulers.mainThread()
    }

    override fun computationScheduler() : Scheduler {

        return Schedulers.computation()
    }
}