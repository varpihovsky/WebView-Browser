package com.example.webviewbrowser.activity

import android.view.KeyEvent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.webviewbrowser.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityTest {

    @get:Rule
    val mainActivityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testClickOnNewPageButton() {
        onView(withId(R.id.newPageButton)).perform(click())
    }

    @Test
    fun testWriteUrlToAddressBar() {
        runBlocking {
            delay(1000)

            onView(withId(R.id.addressBar)).perform(
                clearText(),
                typeText("https://m.facebook.com/"),
                pressKey(KeyEvent.KEYCODE_ENTER)
            ).check(matches(withText("https://m.facebook.com/")))

            delay(1000)
        }
    }
}