package com.example.locationtrackerapp.baseClass

import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import com.example.locationtrackerapp.utils.internerChecker.ConnectivityReceiver
import com.example.locationtrackerapp.utils.internerChecker.ConnectivityReceiverListener

abstract class baseActivity : AppCompatActivity() , ConnectivityReceiverListener {

    // connectivity Receiver class
    private var connectivityReceiver = ConnectivityReceiver

    // call the services on internet present
    abstract fun callInitialServices(b: Boolean)

    // call the observers onCreate
    abstract fun callObservers()


    override fun onStart() {
        super.onStart()

        // register connectivity receiver
        registerConnectivityReceiver(true)

        callObservers()
    }

    override fun onDestroy() {
        registerConnectivityReceiver(false)
        super.onDestroy()
    }

    private fun registerConnectivityReceiver(isRegister: Boolean) {
        if (isRegister) {
            // set network listener callback
            connectivityReceiver.connectivityReceiverListener = this
            // register the network check broadcast receiver
            registerReceiver(
                connectivityReceiver,
                IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
            )
        } else {

            try {
                // unregister the connectivity receiver
                unregisterReceiver(connectivityReceiver)
            } catch (e: IllegalArgumentException) {

            }
        }
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {

        if (isConnected) {
            callInitialServices(true)

        } else {
            callInitialServices(false)
        }
    }
}