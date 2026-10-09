package org.baltimorehackspace.bahaamazon

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import org.json.JSONArray
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private var failedHandoffs = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        failedHandoffs = savedInstanceState?.getInt("failed_handoffs", 0) ?: 0

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

                showGearShuffle()
            }

            exitButton.setOnClickListener {
                finish()
            }

            return
        }

        showGearShuffle()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt("failed_handoffs", failedHandoffs)
        super.onSaveInstanceState(outState)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        // Incoming data never authorizes an affiliate handoff.
        if (getSharedPreferences("baha_prefs", MODE_PRIVATE)
                .getBoolean("affiliate_opt_in", false)) {
            showGearShuffle()
        }
    }

    private fun showGearShuffle() {
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.sendItButton).setOnClickListener { button ->
            button.isEnabled = false
            try {
                openAmazon()
            } catch (_: ActivityNotFoundException) {
                button.isEnabled = true
                showHandoffError(R.string.amazon_unavailable)
            } catch (_: SecurityException) {
                button.isEnabled = true
                showHandoffError(R.string.amazon_launch_blocked)
            }
        }
    }

    private fun showHandoffError(message: Int) {
        // Count failed explicit taps; retries remain available indefinitely.
        failedHandoffs = (failedHandoffs + 1).coerceAtMost(3)
        if (failedHandoffs >= 3) {
            AlertDialog.Builder(this)
                .setTitle(R.string.amazon_launch_unsuccessful)
                .setMessage(R.string.amazon_retry_message)
                .setPositiveButton(android.R.string.ok, null)
                .show()
        } else {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        }
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
