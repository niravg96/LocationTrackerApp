package com.example.locationtrackerapp.utils.internerChecker

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager

object ConnectivityReceiver : BroadcastReceiver() {

    var connectivityReceiverListener: ConnectivityReceiverListener? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        if (connectivityReceiverListener != null) {
            connectivityReceiverListener!!.onNetworkConnectionChanged(
                isConnectedOrConnecting(
                    context!!
                )
            )
        }
    }
    private fun isConnectedOrConnecting(context: Context): Boolean {
        val connMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connMgr.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnectedOrConnecting
    }
}