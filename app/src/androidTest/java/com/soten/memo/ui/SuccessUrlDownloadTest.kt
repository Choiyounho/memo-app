package com.soten.memo.ui


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import androidx.test.runner.AndroidJUnit4
import com.soten.memo.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class SuccessUrlDownloadTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun mainActivityTest() {
        val floatingActionButton = onView(
            allOf(withId(R.id.memoEditButton),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment_activity_main),
                        0),
                    1),
                isDisplayed()))
        floatingActionButton.perform(click())

        val appCompatImageView = onView(
            allOf(withId(R.id.urlButton),
                childAtPosition(
                    allOf(withId(R.id.buttonContainer),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            4)),
                    2),
                isDisplayed()))
        appCompatImageView.perform(click())

        val appCompatEditText = onView(
            allOf(withId(R.id.inputUrl),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0),
                    0),
                isDisplayed()))
        appCompatEditText.perform(longClick())

        val appCompatEditText2 = onView(
            allOf(withId(R.id.inputUrl),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0),
                    0),
                isDisplayed()))
        appCompatEditText2.perform(replaceText("https://media.istockphoto.com/photos/panthera-tigris-sumatrae-female-with-cub-laying-down-on-grass-picture-id1226780763"),
            closeSoftKeyboard())

        val materialButton = onView(
            allOf(withId(R.id.addUrlButton), withText("Add Url"),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0),
                    1),
                isDisplayed()))
        materialButton.perform(click())

        val appCompatEditText3 = onView(
            allOf(withId(R.id.editMemoTitleText),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment_activity_main),
                        0),
                    0),
                isDisplayed()))
        appCompatEditText3.perform(replaceText("tiger"), closeSoftKeyboard())

        val appCompatEditText4 = onView(
            allOf(withId(R.id.editMemoDescriptionText),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment_activity_main),
                        0),
                    1),
                isDisplayed()))
        appCompatEditText4.perform(replaceText("family"), closeSoftKeyboard())

        val materialButton2 = onView(
            allOf(withId(R.id.submitButton), withText("submit"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment_activity_main),
                        0),
                    2),
                isDisplayed()))
        materialButton2.perform(click())
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int,
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
