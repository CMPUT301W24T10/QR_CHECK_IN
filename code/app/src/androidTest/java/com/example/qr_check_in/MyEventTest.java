package com.example.qr_check_in;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.Manifest;
import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.GrantPermissionRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith( AndroidJUnit4.class)
@LargeTest
public class MyEventTest {
    @Before
    public void launchActivityWithIntent() {
        // Create an Intent that includes the extras.
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), MyEventActivity.class);
        intent.putExtra("eventId", "AyHgTJJHXgQZxplpCqnI");
        intent.putExtra("userId", "c9ca870565fc48cb");
        intent.putExtra("userType", "organizer");
        // Launch the activity with the intent
        ActivityScenario<EventActivity> scenario = ActivityScenario.launch(intent);
    }
    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.POST_NOTIFICATIONS);

    @Test
    public void testDrawer() {
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_signups)).check(matches(isDisplayed()));
        onView(withId(R.id.nav_signups)).perform(click());
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_attending)).check(matches(isDisplayed()));
        onView(withId(R.id.nav_attending)).perform(click());
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_organizer)).check(matches(isDisplayed()));
        onView(withId(R.id.nav_organizer)).perform(click());
    }
    @Test
    public void backToMain() {
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.MyEventsBackButton)).check(matches(isDisplayed()));
        onView(withId(R.id.MyEventsBackButton)).perform(click());
        onView(withId(R.id.checkInButton)).check(matches(isDisplayed()));
    }
}
