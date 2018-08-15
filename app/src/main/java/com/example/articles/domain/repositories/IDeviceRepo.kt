package com.example.articles.domain.repositories


interface IDeviceRepo : IRepo {

    val isNetworkConnected: Boolean
}
