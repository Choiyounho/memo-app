package com.soten.memo.ui


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
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
@RunWith(AndroidJUnit4ClassRunner::class)
class CreateMemoTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun createMemoTest() {
        val floatingActionButton = onView(
            allOf(withId(R.id.memoEditButton),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment_activity_main),
                        0),
                    1),
                isDisplayed()))
        floatingActionButton.perform(click())

        val appCompatEditText = onView(
            allOf(withId(R.id.editMemoTitleText),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("androidx.coordinatorlayout.widget.CoordinatorLayout")),
                        0),
                    0),
                isDisplayed()))
        appCompatEditText.perform(replaceText("title"), closeSoftKeyboard())

        val appCompatEditText2 = onView(
            allOf(withId(R.id.editMemoDescriptionText),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("androidx.coordinatorlayout.widget.CoordinatorLayout")),
                        0),
                    1),
                isDisplayed()))
        appCompatEditText2.perform(replaceText("description"), closeSoftKeyboard())

        val materialButton = onView(
            allOf(withId(R.id.submitButton), withText("submit"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("androidx.coordinatorlayout.widget.CoordinatorLayout")),
                        0),
                    2),
                isDisplayed()))
        materialButton.perform(click())
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
