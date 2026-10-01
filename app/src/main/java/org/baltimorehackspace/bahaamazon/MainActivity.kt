package org.baltimorehackspace.bahaamazon

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import org.json.JSONArray
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Show the disclosure until they opt in once.
        val prefs = getSharedPreferences("baha_prefs", MODE_PRIVATE)
        val hasOptedIn = prefs.getBoolean("affiliate_opt_in", false)

        if (!hasOptedIn) {
            setContentView(R.layout.activity_disclosure)

            val optInButton = findViewById<Button>(R.id.optInButton)
            val exitButton = findViewById<Button>(R.id.exitButton)

            optInButton.setOnClickListener {
                prefs.edit {
                    putBoolean("affiliate_opt_in", true)
                }

                openAmazon()
            }

            exitButton.setOnClickListener {
                finish()
            }

            return
        }

        openAmazon()
    }

    private fun openAmazon() {

        // Pull a random ASIN. If the file is empty or broken, use the fallback.
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

        // Build the tagged Amazon link and hand it off.
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
