package com.mlukov.articles.utils

import io.reactivex.Scheduler

interface ISchedulersProvider {

     fun ioScheduler() : Scheduler

     fun uiScheduler() : Scheduler

     fun computationScheduler() : Scheduler
}