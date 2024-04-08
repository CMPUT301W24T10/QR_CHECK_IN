package com.example.qr_check_in;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import com.example.qr_check_in.ModelClasses.Attendee;
import com.example.qr_check_in.ModelClasses.Event;
import com.example.qr_check_in.ModelClasses.Organizer;
import com.example.qr_check_in.ModelClasses.User;
import com.example.qr_check_in.data.AppDatabase;

import com.example.qr_check_in.ui.home.HomeViewModel;
import com.example.qr_check_in.ui.listOfAttendee.ListOfAttendeesViewModel;
import com.google.android.material.navigation.NavigationView;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.example.qr_check_in.databinding.ActivityEventBinding;

import java.util.Map;
import java.util.Objects;

public class EventActivity extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityEventBinding binding;
    private Event event;
    private User user;
    private AppDatabase db;
    private ListOfAttendeesViewModel listOfAttendeesViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new AppDatabase();
        listOfAttendeesViewModel = new ViewModelProvider(this).get(ListOfAttendeesViewModel.class);
        String eventId = getIntent().getStringExtra("eventId");
        String userType = getIntent().getStringExtra("userType");
        if (eventId != null) {
            listOfAttendeesViewModel.setEventId(eventId);
            listOfAttendeesViewModel.setUserId(getIntent().getStringExtra("userId"));
            // Obtain HomeViewModel and set eventId here
            HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
            homeViewModel.setEventId(eventId);
        }


        binding = ActivityEventBinding.inflate(getLayoutInflater());
         setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarEvent.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        /**
         * created icon for notification in the dashboard to send notification
         */
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_edit_profile,R.id.nav_announcements,R.id.nav_list_of_attendees,R.id.notificationFragment2, R.id.nav_signups, R.id.nav_events, R.id.map, R.id.shareQrCodeItem, R.id.checkOutButton)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_event);
        navController.setGraph(navController.getGraph()); // Pass data to the navController
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // hide items from the navigation drawer if the user is an attendee
        if(Objects.equals(userType, "Attendee")) {
            hideItem();
        }

        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.checkOutButton).setOnMenuItemClickListener(item -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            this.finish();
            return true;
        });
    }

    public void hideItem() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.nav_list_of_attendees).setVisible(false);
        nav_Menu.findItem(R.id.nav_events).setVisible(false);
        nav_Menu.findItem(R.id.shareQrCodeItem).setVisible(false);
        nav_Menu.findItem(R.id.nav_signups).setVisible(false);
        nav_Menu.findItem(R.id.notificationFragment2).setVisible(false);
        nav_Menu.findItem(R.id.map).setVisible(false);

    }

    public User setUser(String userId, String name,String role) {
        if(Objects.equals(role, "organizer")) {
            user = new Organizer(name, userId);
        }
        else {
            user = new Attendee(name, userId);
        }
        return user;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.event, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_event);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
