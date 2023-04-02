package com.shaza.minipostman.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build


object CheckInternetConnection {

    fun isInternetAvailable(context: Context): Boolean {
        return isNetworkAvailable(context)
    }

    private fun isNetworkAvailable(context: Context?): Boolean {
        if (context == null) return false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return if (connectivityManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val capabilities =
                    connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        return true
                    }
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        true
                    } else capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
                } else {
                    false
                }
            } else {
                val info = connectivityManager.activeNetworkInfo
                info != null && info.isConnected
            }
        } else false
    }

}