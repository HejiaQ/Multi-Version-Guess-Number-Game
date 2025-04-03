package com.example.guessnumbergametraditionalui

import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.testify.annotation.ScreenshotInstrumentation
import dev.testify.scenario.ScreenshotScenarioRule
import androidx.test.core.app.launchActivity

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Rule

/**
A screenshot testing example
 */

@RunWith(AndroidJUnit4::class)
class MainActivityScreenshotTest {

    @get:Rule val rule = ScreenshotScenarioRule()

    @ScreenshotInstrumentation
    @Test
    fun login() {
        launchActivity<MainActivity>().use { scenario ->
            rule.withScenario(scenario).assertSame()
        }
    }
}

/*
Comparing diff:
   Black pixels are identical between the baseline and test image
   Grey pixels have been excluded from the comparison
   Yellow pixels are different, but within the Exactness threshold
   Red pixels are different
*/