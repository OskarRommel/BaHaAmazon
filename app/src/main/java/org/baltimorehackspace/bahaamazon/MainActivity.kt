package org.baltimorehackspace.bahaamazon

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //get uri from intent
        val incomingUri = intent.data ?: return
        //get query parameter from uri
        val affiliatelink = incomingUri.getQueryParameter("url") ?: return

        val passToTaskerIntent = Intent()
        passToTaskerIntent.setAction("org.baltimorehackspace.bahaamazon.PASS_TO_TASKER")

        passToTaskerIntent.putExtra("url", affiliatelink)
        sendBroadcast(passToTaskerIntent)

        finish()
    }
}
