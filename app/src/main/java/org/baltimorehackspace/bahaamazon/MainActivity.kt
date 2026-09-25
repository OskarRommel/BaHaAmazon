package org.baltimorehackspace.bahaamazon

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //get URI from intent
        val incomingUri = intent.data ?: return
        //get affiliate URL from incoming URI
        val affiliatelink = incomingUri.getQueryParameter("url") ?: return
        // Open affiliate URL with the default Android handler
        val amazonUri = affiliatelink.toUri()
        val openAmazonIntent = Intent(Intent.ACTION_VIEW, amazonUri)
        startActivity(openAmazonIntent)
        finish()
    }
}
