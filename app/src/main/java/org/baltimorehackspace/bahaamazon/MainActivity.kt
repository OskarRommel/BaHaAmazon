package org.baltimorehackspace.bahaamazon

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val affiliateUrl = "https://www.amazon.com/dp/B0D4YMYZB1?ref=ppx_yo2ov_dt_b_fed_asin_title&tag=baltimorehack-20"
        val amazonUri = affiliateUrl.toUri()
        val openAmazonIntent = Intent(Intent.ACTION_VIEW, amazonUri)
        startActivity(openAmazonIntent)
        finish()
    }
}
