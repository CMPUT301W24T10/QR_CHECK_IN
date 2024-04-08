package com.example.qr_check_in;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.instanceOf;

import android.Manifest;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.GrantPermissionRule;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith( AndroidJUnit4.class)
@LargeTest
public class EventActitvityTest {
    // This is the rule that tells Espresso to launch EventActivity before each test
    // I want to pass two parameters to the EventActivity, the event name and the event description
    // I will use the putExtra method to pass these parameters
    // I will use the getExtra method to retrieve these parameters

    @Before
    public void launchActivityWithIntent() {
        // Create an Intent that includes the extras.
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), EventActivity.class);
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
    public void testHomeFragment() {
        // Use Espresso to test the EventActivity
        onView(withId(R.id.button_more_details)).check(matches(isDisplayed()));
    }
    @Test
    public void testListOfAttendeesFragment() {
        // Use Espresso to test the EventActivity
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_list_of_attendees)).check(matches(isDisplayed()));
        onView(withId(R.id.nav_list_of_attendees)).perform(click());// press hamburger button
        onView(withId(R.id.list_of_attendees)).check(matches(isDisplayed()));
    }
    @Test
    public void testEventsFragment() {
        // Use Espresso to test the EventActivity
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_events)).check(matches(isDisplayed()));
        onView(withId(R.id.nav_events)).perform(click());// press hamburger button
    }
    @Test
    public void testShareQrCodeFragment() {
        // Use Espresso to test the EventActivity
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.shareQrCodeItem)).check(matches(isDisplayed()));
        onView(withId(R.id.shareQrCodeItem)).perform(click());// press hamburger button
        onView(withId(R.id.ShowQRCode)).check(matches(isDisplayed()));
    }
    @Test
    public void testSignupsFragment() {
        // Use Espresso to test the EventActivity
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_signups)).check(matches(isDisplayed()));
        onView(withId(R.id.nav_signups)).perform(click());// press hamburger button
        onView(withId(R.id.list_of_attendees)).check(matches(isDisplayed()));
    }

    public static Matcher<View> withEmptyText() {
        return new TypeSafeMatcher<View>() {

            @Override
            public boolean matchesSafely(View view) {
                if (!(view instanceof EditText)) {
                    return false;
                }

                String text = ((EditText) view).getText().toString();
                return text.isEmpty();
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with empty text");
            }
        };
    }
    @Test
    public void testNotification() {
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.notificationFragment2)).check(matches(isDisplayed()));
        onView(withId(R.id.notificationFragment2)).perform(click());// press hamburger button
        onView(withId(R.id.headerSendNotifications)).check(matches(isDisplayed()));

        // Find the EditText that is an instance of NotificationTitle and has empty text, then type into it
        onView(allOf(instanceOf(EditText.class), withId(R.id.NotificationTitle), withEmptyText())).perform(typeText("Test Notification title"));
        closeSoftKeyboard();
        onView(allOf(instanceOf(EditText.class), withId(R.id.NotificationText), withEmptyText())).perform(typeText("Test Notification body"));
        closeSoftKeyboard();
        onView(withId(R.id.sendButton)).perform(click());
    }
    @Test
    public void testMapFragment() {
        // Use Espresso to test the EventActivity
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.map)).check(matches(isDisplayed()));
    }
    @Test
    public void editProfile() {
        // Use Espresso to test the EventActivity
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_edit_profile)).check(matches(isDisplayed()));
        onView(withId(R.id.nav_edit_profile)).perform(click());// press hamburger button
        onView(withId(R.id.editTextName)).check(matches(isDisplayed()));
        onView(withId(R.id.editTextName)).perform(typeText("Test Name"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.editTextPhone)).perform(typeText("Test Phone"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.editTextEmailAddress)).perform(typeText("Test Email"),ViewActions.closeSoftKeyboard());
        onView(withId(R.id.buttonSave)).perform(click());
    }
    @Test
    public void testCheckOutButton() {
        // Use Espresso to test the EventActivity
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.checkOutButton)).check(matches(isDisplayed()));
        onView(withId(R.id.checkOutButton)).perform(click());// press hamburger button
        onView(withId(R.id.checkInButton)).check(matches(isDisplayed()));
    }



}
