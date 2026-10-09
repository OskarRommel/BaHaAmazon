package org.baltimorehackspace.bahaamazon

import android.app.Activity
import android.app.Instrumentation.ActivityResult
import android.content.Intent
import android.net.Uri
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.not
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GearShuffleTest {
    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val prefs get() = context.getSharedPreferences("baha_prefs", 0)

    private fun outboundIntents() = Intents.getIntents().filter {
        it.component?.className != MainActivity::class.java.name
    }

    @Before fun setup() {
        prefs.edit().clear().commit()
        Intents.init()
        Intents.intending(allOf(
            hasAction(Intent.ACTION_VIEW),
            not(hasComponent(MainActivity::class.java.name))
        ))
            .respondWith(ActivityResult(Activity.RESULT_OK, null))
    }

    @After fun cleanup() {
        Intents.release()
        prefs.edit().clear().commit()
    }

    @Test fun optInRequiresSeparateSendItTap() {
        ActivityScenario.launch(MainActivity::class.java).use {
            onView(withId(R.id.optInButton)).perform(click())
            assertTrue(prefs.getBoolean("affiliate_opt_in", false))
            onView(withId(R.id.affiliateDisclosure)).check(matches(isDisplayed()))
            assertTrue(outboundIntents().isEmpty())
            onView(withId(R.id.sendItButton)).perform(click())
            val outbound = outboundIntents().single()
            assertEquals(Intent.ACTION_VIEW, outbound.action)
            assertEquals("https", outbound.data?.scheme)
            assertEquals("www.amazon.com", outbound.data?.host)
            assertEquals("baltimorehack-20", outbound.data?.getQueryParameter("tag"))
            assertTrue(outbound.data?.path?.startsWith("/dp/") == true)
        }
    }

    @Test fun optedInInboundLaunchAndRecreationDoNotOpenAmazon() {
        prefs.edit().putBoolean("affiliate_opt_in", true).commit()
        val incoming = Intent(context, MainActivity::class.java)
            .setAction(Intent.ACTION_VIEW)
            .setData(Uri.parse("https://example.com/untrusted?tag=other"))
        ActivityScenario.launch<MainActivity>(incoming).use { scenario ->
            onView(withId(R.id.sendItButton)).check(matches(isDisplayed()))
            onView(withId(R.id.affiliateDisclosure)).check(matches(isDisplayed()))
            scenario.onActivity {
                it.startActivity(Intent(incoming).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))
            }
            onView(withId(R.id.affiliateDisclosure)).check(matches(isDisplayed()))
            scenario.recreate()
            onView(withId(R.id.affiliateDisclosure)).check(matches(isDisplayed()))
            assertTrue(outboundIntents().isEmpty())
        }
    }

    @Test fun optingOutDoesNotOpenAmazonOrPersistConsent() {
        ActivityScenario.launch(MainActivity::class.java).use {
            onView(withId(R.id.exitButton)).perform(click())
            assertFalse(prefs.getBoolean("affiliate_opt_in", false))
            assertTrue(outboundIntents().isEmpty())
        }
    }
}
