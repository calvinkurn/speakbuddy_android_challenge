package jp.speakbuddy.factsearcher.ui

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import jp.speakbuddy.factsearcher.R
import jp.speakbuddy.factsearcher.ui.screen.FACT_SCREEN_FAVORITE_BUTTON_TAG
import jp.speakbuddy.factsearcher.ui.screen.FACT_SCREEN_UPDATE_FACT_BUTTON_TAG
import jp.speakbuddy.factsearcher.ui.utils.LocaleManager
import jp.speakbuddy.factsearcher.ui.view.FactActivity
import jp.speakbuddy.factsearcher.ui.widget.FACT_WIDGET_CONTENT_TAG
import jp.speakbuddy.factsearcher.ui.widget.FACT_WIDGET_LIKE_BUTTON
import jp.speakbuddy.factsearcher.ui.widget.FACT_WIDGET_TAG
import jp.speakbuddy.factsearcher.ui.widget.HEADER_BACK_NAVIGATION_TAG
import jp.speakbuddy.factsearcher.ui.widget.LANGUAGE_WIDGET_ICON
import jp.speakbuddy.factsearcher.ui.widget.LANGUAGE_WIDGET_ITEM_JP
import jp.speakbuddy.factsearcher.utils.getLocalizedString
import jp.speakbuddy.factsearcher.utils.setLocale
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Locale

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class TestActivity {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setup() {
        hiltRule.inject()
    }

    // test fetch new data & load previous data
    @Test
    fun testFactActivityLoadsCorrectly() {
        launchActivity {
            val node = composeTestRule.onNodeWithTag(FACT_WIDGET_CONTENT_TAG).fetchSemanticsNode()
            val text = node.config.getOrNull(SemanticsProperties.Text)

            composeTestRule.onNodeWithTag(FACT_WIDGET_TAG).assertIsDisplayed()
            assertTrue(!text.isNullOrEmpty())

            composeTestRule.onNodeWithTag(FACT_SCREEN_UPDATE_FACT_BUTTON_TAG).performClick()

            val node2 = composeTestRule.onNodeWithTag(FACT_WIDGET_CONTENT_TAG).fetchSemanticsNode()
            val text2 = node2.config.getOrNull(SemanticsProperties.Text)

            composeTestRule.onNodeWithTag(FACT_WIDGET_TAG).assertIsDisplayed()
            assertTrue(!text2.isNullOrEmpty())
        }
    }

    // test like fact
    @Test
    fun testLikedFact() {
        launchActivity {
            composeTestRule.onNodeWithTag(FACT_WIDGET_TAG).assertIsDisplayed()

            val node = composeTestRule.onNodeWithTag(FACT_WIDGET_CONTENT_TAG).fetchSemanticsNode()
            val text = node.config.getOrNull(SemanticsProperties.Text)
            assertTrue(!text.isNullOrEmpty())

            composeTestRule.onNodeWithTag(FACT_WIDGET_LIKE_BUTTON).performClick()
            composeTestRule.onNodeWithTag(FACT_SCREEN_FAVORITE_BUTTON_TAG).performClick()

            val node2 = composeTestRule.onNodeWithTag(FACT_WIDGET_CONTENT_TAG).fetchSemanticsNode()
            val text2 = node2.config.getOrNull(SemanticsProperties.Text)
            assertTrue(!text.isNullOrEmpty())

            composeTestRule.onNodeWithTag(FACT_WIDGET_TAG).assertIsDisplayed()
            assertEquals(text2, text)
        }
    }

    // test favorite page unlike fact
    @Test
    fun testFavoritePage() {
        launchActivity {
            composeTestRule.onNodeWithTag(FACT_WIDGET_TAG).assertIsDisplayed()

            val node = composeTestRule.onNodeWithTag(FACT_WIDGET_CONTENT_TAG).fetchSemanticsNode()
            val text = node.config.getOrNull(SemanticsProperties.Text)
            assertTrue(!text.isNullOrEmpty())

            composeTestRule.onNodeWithTag(FACT_WIDGET_LIKE_BUTTON).performClick()
            composeTestRule.onNodeWithTag(FACT_SCREEN_FAVORITE_BUTTON_TAG).performClick()

            val node2 = composeTestRule.onNodeWithTag(FACT_WIDGET_CONTENT_TAG).fetchSemanticsNode()
            val text2 = node2.config.getOrNull(SemanticsProperties.Text)
            assertTrue(!text.isNullOrEmpty())

            composeTestRule.onNodeWithTag(FACT_WIDGET_TAG).assertIsDisplayed()
            assertEquals(text2, text)

            composeTestRule.onNodeWithTag(FACT_WIDGET_LIKE_BUTTON).performClick()
            composeTestRule.onNodeWithTag(FACT_WIDGET_TAG).assertIsNotDisplayed()
        }
    }

    // test change language to JP
    @Test
    fun testChangeLanguage() {
        launchActivity {
            composeTestRule.onNodeWithTag(FACT_WIDGET_TAG).assertIsDisplayed()

            val node = composeTestRule.onNodeWithTag(FACT_WIDGET_CONTENT_TAG).fetchSemanticsNode()
            val text = node.config.getOrNull(SemanticsProperties.Text)
            assertTrue(!text.isNullOrEmpty())

            composeTestRule.onNodeWithTag(LANGUAGE_WIDGET_ICON).performClick()
            composeTestRule.waitForIdle()
            composeTestRule.onNodeWithTag(LANGUAGE_WIDGET_ITEM_JP).performClick()
            composeTestRule.waitForIdle()

            val appContext = InstrumentationRegistry.getInstrumentation().targetContext
            val localizedContext = appContext.setLocale(Locale.JAPAN)
            val pageTitle = localizedContext.getLocalizedString(R.string.fact_page_title)

            composeTestRule.onNodeWithText(pageTitle).assertIsDisplayed()
        }
    }

    // test change language to jp on favorite page
    @Test
    fun testFavoritePageLanguage() {
        launchActivity {
            composeTestRule.onNodeWithTag(FACT_WIDGET_TAG).assertIsDisplayed()

            val node = composeTestRule.onNodeWithTag(FACT_WIDGET_CONTENT_TAG).fetchSemanticsNode()
            val text = node.config.getOrNull(SemanticsProperties.Text)
            assertTrue(!text.isNullOrEmpty())

            composeTestRule.onNodeWithTag(FACT_WIDGET_LIKE_BUTTON).performClick()
            composeTestRule.onNodeWithTag(FACT_SCREEN_FAVORITE_BUTTON_TAG).performClick()

            val node2 = composeTestRule.onNodeWithTag(FACT_WIDGET_CONTENT_TAG).fetchSemanticsNode()
            val text2 = node2.config.getOrNull(SemanticsProperties.Text)
            assertTrue(!text.isNullOrEmpty())

            composeTestRule.onNodeWithTag(FACT_WIDGET_TAG).assertIsDisplayed()
            assertEquals(text2, text)

            composeTestRule.onNodeWithTag(LANGUAGE_WIDGET_ICON).performClick()
            composeTestRule.waitForIdle()
            composeTestRule.onNodeWithTag(LANGUAGE_WIDGET_ITEM_JP).performClick()
            composeTestRule.waitForIdle()

            val appContext = InstrumentationRegistry.getInstrumentation().targetContext
            val localizedContext = appContext.setLocale(Locale.JAPAN)
            val pageTitle = localizedContext.getLocalizedString(R.string.favorite_page_title)

            composeTestRule.onNodeWithText(pageTitle).assertIsDisplayed()
        }
    }

    // test change language and back to previous page
    @Test
    fun testBackNavigationWithLanguageUpdate() {
        launchActivity {
            LocaleManager.updateLocale(Locale.US, LocaleManager.PAGE_ID_FACT)

            composeTestRule.onNodeWithTag(FACT_WIDGET_TAG).assertIsDisplayed()

            val node = composeTestRule.onNodeWithTag(FACT_WIDGET_CONTENT_TAG).fetchSemanticsNode()
            val text = node.config.getOrNull(SemanticsProperties.Text)
            assertTrue(!text.isNullOrEmpty())

            composeTestRule.onNodeWithTag(FACT_WIDGET_LIKE_BUTTON).performClick()
            composeTestRule.onNodeWithTag(FACT_SCREEN_FAVORITE_BUTTON_TAG).performClick()

            val node2 = composeTestRule.onNodeWithTag(FACT_WIDGET_CONTENT_TAG).fetchSemanticsNode()
            val text2 = node2.config.getOrNull(SemanticsProperties.Text)
            assertTrue(!text.isNullOrEmpty())

            composeTestRule.onNodeWithTag(FACT_WIDGET_TAG).assertIsDisplayed()
            assertEquals(text2, text)

            composeTestRule.onNodeWithTag(LANGUAGE_WIDGET_ICON).performClick()
            composeTestRule.waitForIdle()
            composeTestRule.onNodeWithTag(LANGUAGE_WIDGET_ITEM_JP).performClick()
            composeTestRule.waitForIdle()

            composeTestRule.onNodeWithTag(HEADER_BACK_NAVIGATION_TAG).performClick()

            val appContext = InstrumentationRegistry.getInstrumentation().targetContext
            val localizedContext = appContext.setLocale(Locale.JAPAN)
            val pageTitle = localizedContext.getLocalizedString(R.string.fact_page_title)

            composeTestRule.waitForIdle()
            composeTestRule.onNodeWithText(pageTitle).assertIsDisplayed()
        }
    }

    private fun launchActivity(action: () -> Unit) {
        ActivityScenario.launch(FactActivity::class.java).use { scenario ->
            scenario.onActivity { activity ->
                assertTrue(activity is FactActivity)
            }

            action()
        }
    }
}