package com.example.locationtrackerapp.baseClass

import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.fragment.app.Fragment
import com.example.locationtrackerapp.utils.internerChecker.ConnectivityReceiver
import com.example.locationtrackerapp.utils.internerChecker.ConnectivityReceiverListener

abstract class BaseFragment : Fragment(), ConnectivityReceiverListener {

    // connectivity Receiver class
    private var connectivityReceiver = ConnectivityReceiver


    override fun onNetworkConnectionChanged(isConnected: Boolean) {

        if (isConnected) {
            callInitialServices(true)
        } else {
            callInitialServices(false)
        }
    }

    // call the services on internet present
    abstract fun callInitialServices(connectionStatus: Boolean)

    override fun onResume() {
        super.onResume()
        registerConnectivityReceiver(true)
    }

    private fun registerConnectivityReceiver(isRegister: Boolean) {
        if (isRegister) {
            // set network listener callback
            connectivityReceiver.connectivityReceiverListener = this
            // register the network check broadcast receiver
            requireActivity().registerReceiver(
                connectivityReceiver,
                IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
            )
        } else {

            try {
                // unregister the connectivity receiver
                requireActivity().unregisterReceiver(connectivityReceiver)
            } catch (e: IllegalArgumentException) {

            }

        }
    }
}