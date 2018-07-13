package gov.ap.tms.annadanam.controllers

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.os.Build
import android.provider.Settings
import android.support.v7.app.AlertDialog
import android.text.format.Formatter
import java.net.NetworkInterface

import gov.ap.tms.annadanam.BuildConfig

open class BaseActivity : Activity() {

    val isTimeAutomatic: Boolean
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Settings.Global.getInt(this.contentResolver, Settings.Global.AUTO_TIME, 0) == 1
        } else {
            android.provider.Settings.System.getInt(this.contentResolver, android.provider.Settings.System.AUTO_TIME, 0) == 1
        }

    val isNetwork: Boolean
        get() {

            val connection = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            if (connection.getNetworkInfo(0).state == android.net.NetworkInfo.State.CONNECTED ||
                    connection.getNetworkInfo(0).state == android.net.NetworkInfo.State.CONNECTING ||
                    connection.getNetworkInfo(1).state == android.net.NetworkInfo.State.CONNECTING ||
                    connection.getNetworkInfo(1).state == android.net.NetworkInfo.State.CONNECTED) {
                return true

            } else if (connection.getNetworkInfo(0).state == android.net.NetworkInfo.State.DISCONNECTED || connection.getNetworkInfo(1).state == android.net.NetworkInfo.State.DISCONNECTED) {
                return false

            }
            return false
        }

    val androidDeviceID: String
        get() = Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID)

    val versionID: String
        get() = BuildConfig.VERSION_NAME

    val ipAddress: String
        get() = deviceIP

    private val deviceIP: String
        get() {

            var ipAddress = "0.0.00.00"

            var WIFI = false
            var MOBILE = false
            val CM = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = CM.allNetworkInfo
            for (netInfo in networkInfo) {
                if (netInfo.typeName.equals("WIFI", ignoreCase = true))
                    if (netInfo.isConnected)
                        WIFI = true
                if (netInfo.typeName.equals("MOBILE", ignoreCase = true))
                    if (netInfo.isConnected)
                        MOBILE = true
            }
            if (WIFI) {
                ipAddress = wifiIPAddress()
            }
            if (MOBILE) {
                ipAddress = mobileIPAddress()
            }

            return ipAddress
        }

    fun showInternetDialog() {

        val networkDialogBuilder = AlertDialog.Builder(this)
                 networkDialogBuilder.setMessage("No internet connection on your device. Would you like to enable it?")
                 .setTitle("No Internet Connection").setCancelable(false).setCancelable(false)
                 .setPositiveButton(" Enable ") { dialog, id -> startActivity(Intent(Settings.ACTION_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)) }

        networkDialogBuilder.setNegativeButton(" Cancel ") { dialog, id ->
                 dialog.cancel()
                 finish()
        }

        val alert = networkDialogBuilder.create()
        alert.show()

    }

    fun showAutoTimeDialog() {
        val timeDialogBuilder = AlertDialog.Builder(this)
        timeDialogBuilder.setMessage("Disabled auto time & date on your device. Would you like to enable it?")
                .setTitle("Auto time disabled").setCancelable(false).setCancelable(false)
                .setPositiveButton(" Enable ") { dialog, id -> startActivity(Intent(Settings.ACTION_DATE_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)) }

        timeDialogBuilder.setNegativeButton(" Cancel ") { dialog, id ->
            dialog.cancel()
            finish()
        }

        val timeAlert = timeDialogBuilder.create()
        timeAlert.show()

    }

    private fun wifiIPAddress(): String {

        var wifiIPAddress = "0.0.00.00"

        try {

            val wm = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
            wifiIPAddress = Formatter.formatIpAddress(wm.connectionInfo.ipAddress)

        } catch (e: Exception) {
            e.printStackTrace()
            return wifiIPAddress
        }

        return wifiIPAddress

    }

    private fun mobileIPAddress(): String {

        var mobileIPAddress = "0.0.00.00"

        try {

            val en = NetworkInterface.getNetworkInterfaces()
            while (en.hasMoreElements()) {
                val networkinterface = en.nextElement()
                val enumIpAddr = networkinterface.inetAddresses
                while (enumIpAddr.hasMoreElements()) {
                    val inetAddress = enumIpAddr.nextElement()
                    if (!inetAddress.isLoopbackAddress) {
                        mobileIPAddress = inetAddress.hostAddress
                        return mobileIPAddress
                    }
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
            return mobileIPAddress
        }

        return mobileIPAddress
    }

}
