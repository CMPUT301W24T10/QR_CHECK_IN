package com.example.qr_check_in;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

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
        intent.putExtra("eventId", "UajAKwNqyyDBUNqFFYD1");
        intent.putExtra("userId", "c9ca870565fc48cb");
        intent.putExtra("userType", "organizer");

        // Launch the activity with the intent
        ActivityScenario<EventActivity> scenario = ActivityScenario.launch(intent);
    }

    @Test
    public void testHomeFrag() {
        // Use Espresso to test the EventActivity

    }

}
