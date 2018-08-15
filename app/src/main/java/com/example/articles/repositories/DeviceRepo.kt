package com.example.articles.repositories

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log

import com.example.articles.domain.repositories.IDeviceRepo

class DeviceRepo(private val context: Context) : IDeviceRepo {

    override val isNetworkConnected: Boolean
        get() {

            var isConnected = false

            try {

                val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val networkInfo = cm.activeNetworkInfo
                isConnected = networkInfo != null && networkInfo.isConnected
            }
            catch (ex: Exception) {

                Log.e(LOG_TAG, ex.message, ex)
            }

            return isConnected
        }

    companion object {

        private val LOG_TAG = DeviceRepo::class.simpleName
    }
}
