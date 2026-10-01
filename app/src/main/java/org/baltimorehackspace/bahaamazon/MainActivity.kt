package org.baltimorehackspace.bahaamazon

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val asin = try {
            val json = assets.open("asins.json")
                .bufferedReader()
                .use { it.readText() }

            val jsonArray = JSONArray(json)

            if (jsonArray.length() > 0) {
                jsonArray
                    .getString(Random.nextInt(jsonArray.length()))
                    .trim()
            } else {
                "B0D4YMYZB1"
            }
        } catch (_: Exception) {
            "B0D4YMYZB1"
        }

        val amazonUri = Uri.Builder()
            .scheme("https")
            .authority("www.amazon.com")
            .path("dp/$asin")
            .appendQueryParameter("tag", "baltimorehack-20")
            .build()

        val openAmazonIntent = Intent(Intent.ACTION_VIEW, amazonUri)

        startActivity(openAmazonIntent)
        finish()
    }
}