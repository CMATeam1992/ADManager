package com.ad_manager

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.widget.Toast

fun Context.isNetworkAvailable(): Boolean {
    val connMgr =
        applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val info: NetworkInfo? = connMgr.activeNetworkInfo
    return info != null && info.isConnected

}

fun Context.isNetworkConnected(): Boolean {
    val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
    cm ?: return false
    val network = cm.activeNetwork
    if (network != null) {
        val networkCapabilities = cm.getNetworkCapabilities(network)
        return networkCapabilities != null
    }

    return false
}


fun Context.toastMessage(message: String) {
    Toast.makeText(
        this,
        message,
        Toast.LENGTH_SHORT
    ).show()
}