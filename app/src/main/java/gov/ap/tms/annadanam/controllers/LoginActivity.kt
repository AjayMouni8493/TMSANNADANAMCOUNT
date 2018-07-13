package gov.ap.tms.annadanam.controllers

import android.os.Bundle
import gov.ap.tms.annadanam.R

class LoginActivity : BaseActivity() {

    var isProcess:Boolean = true



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        println("Device ID::::: $androidDeviceID")
        println("Version ID::::: $versionID")
        println("IP Address::::: $ipAddress")

    }

    override fun onResume() {
        super.onResume()
        if (!isNetwork) {
            isProcess = false
            showInternetDialog()
        }
        if (!isTimeAutomatic) {
            isProcess = false
            showAutoTimeDialog()
        }
    }
}
