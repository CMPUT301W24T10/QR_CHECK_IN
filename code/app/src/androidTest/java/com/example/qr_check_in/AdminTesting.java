package com.example.qr_check_in;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith( AndroidJUnit4.class)
@LargeTest
public class AdminTesting {
    @Rule
    public ActivityScenarioRule<MainActivity> scenario = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testBrowseEvents() {
        // perform a click on the browse event button
        onView(withId(R.id.button_settings)).perform(click());
        // fill admin in the username field
        onView(withId(R.id.etAdminPassword)).perform(typeText("admin"));
        // click the confirm button
        onView(withId(R.id.btnSubmitPassword)).perform(click());
        // click on browse event button
        onView(withId(R.id.btnBrowseEvents)).perform(click());
        // check if the browse event fragment is displayed
        onView(withId(R.id.list_of_events)).check(matches(isDisplayed()));
    }
}
