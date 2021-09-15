package com.soten.memo.ui


import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
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
class MemoModifyTest {

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

        val appCompatEditText = onView(
            allOf(withId(R.id.editMemoTitleText),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment_activity_main),
                        0),
                    0),
                isDisplayed()))
        appCompatEditText.perform(replaceText("title"), closeSoftKeyboard())

        val appCompatEditText2 = onView(
            allOf(withId(R.id.editMemoDescriptionText),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment_activity_main),
                        0),
                    1),
                isDisplayed()))
        appCompatEditText2.perform(replaceText("description"), closeSoftKeyboard())

        val materialButton = onView(
            allOf(withId(R.id.submitButton), withText("submit"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment_activity_main),
                        0),
                    2),
                isDisplayed()))
        materialButton.perform(click())

        val recyclerView = onView(
            allOf(withId(R.id.memoListRecyclerView),
                childAtPosition(
                    withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                    0)))
        recyclerView.perform(actionOnItemAtPosition<ViewHolder>(0, click()))

        val actionMenuItemView = onView(
            allOf(withId(R.id.menu_modify), withContentDescription("modify"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.toolbar),
                        1),
                    0),
                isDisplayed()))
        actionMenuItemView.perform(click())

        val appCompatEditText3 = onView(
            allOf(withId(R.id.editMemoTitleText), withText("title"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment_activity_main),
                        0),
                    0),
                isDisplayed()))
        appCompatEditText3.perform(click())

        val appCompatEditText4 = onView(
            allOf(withId(R.id.editMemoTitleText), withText("title"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment_activity_main),
                        0),
                    0),
                isDisplayed()))
        appCompatEditText4.perform(replaceText("title modify"))

        val appCompatEditText5 = onView(
            allOf(withId(R.id.editMemoTitleText), withText("title modify"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment_activity_main),
                        0),
                    0),
                isDisplayed()))
        appCompatEditText5.perform(closeSoftKeyboard())

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
