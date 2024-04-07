package com.example.qr_check_in;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.qr_check_in.R;
import com.example.qr_check_in.ui.home.HomeFragment;
import org.mockito.Mockito;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.mockito.Mockito.verify;

@RunWith(AndroidJUnit4.class)
public class HomeFragmentTest {

    private NavController mockNavController;

    @Before
    public void setUp() {
        mockNavController = Mockito.mock(NavController.class);

        // Launch the HomeFragment.
        FragmentScenario<HomeFragment> scenario = FragmentScenario.launchInContainer(HomeFragment.class);

        // Set the NavController property on the fragment.
        scenario.onFragment(fragment ->
                Navigation.setViewNavController(fragment.requireView(), mockNavController)
        );
    }

    @Test
    public void testNavigateToEventDetailsFragment() {
        // Perform click action on the "More Details" button.
        Espresso.onView(withId(R.id.button_more_details)).perform(click());

        // Verify that we navigate to the EventDetailsFragment
        verify(mockNavController).navigate(R.id.action_home_to_event_details);

    }
}
