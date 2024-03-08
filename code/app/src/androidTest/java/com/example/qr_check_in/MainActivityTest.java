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
import androidx.test.platform.app.InstrumentationRegistry;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;


import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith( AndroidJUnit4.class)
@LargeTest

public class MainActivityTest {
    @Rule
    public ActivityScenarioRule<MainActivity> scenario = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testAddEvent() {
        // perform a click on the create event button
        onView(withId(R.id.organizeEventButton)).perform(click());

        // fill in the user name, event name, event description and toggle the radio button newQRcode
        onView(withId(R.id.EnterOrganizerName)).perform(typeText("Test Name"));
        onView(withId(R.id.EnterEventName)).perform(typeText("Test Event"));
        onView(withId(R.id.EnterEventDescription)).perform(typeText("Test Description"));
        onView(withId(R.id.button_new_QRcode)).perform(click());
        // add some delay of time after clicking the button
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.confirm_button)).perform(click());
        // check if we navigate to display qrcode fragment
        onView(withId(R.id.header_display_qrcode)).check(matches(isDisplayed()));
        // click the confirm button

    }
}
