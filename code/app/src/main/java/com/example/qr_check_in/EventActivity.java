package com.example.qr_check_in;

import android.os.Bundle;
import android.view.View;
import android.view.Menu;

import com.example.qr_check_in.ModelClasses.Attendee;
import com.example.qr_check_in.ModelClasses.Event;
import com.example.qr_check_in.ModelClasses.Organizer;
import com.example.qr_check_in.ModelClasses.User;
import com.example.qr_check_in.data.AppDatabase;
import com.example.qr_check_in.ui.listOfAttendee.ListOfAttendeesViewModel;
import com.google.android.material.snackbar.Snackbar;
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
        if (eventId != null) {
            listOfAttendeesViewModel.setEventId(eventId);
        }


        binding = ActivityEventBinding.inflate(getLayoutInflater());
         setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarEvent.toolbar);
//        binding.appBarEvent.fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null)
//                        .setAnchorView(R.id.fab).show();
//            }
//        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        /**
         * created icon for notification in the dashboard to send notification
         */
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.nav_list_of_attendees,R.id.notificationFragment2)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_event);
        navController.setGraph(navController.getGraph()); // Pass data to the navController
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // fetch event details from the database
        db.fetchEventDetails( eventId, new AppDatabase.FirestoreDocumentCallback() {
            @Override
            public void onCallback(Map<String, Object> data) {
                // set the event details
                String eventName = (String)data.get("eventName");
                String description = (String)data.get("eventDescription");
                String organizerId = (String)data.get("organizerId");
                // putting arbitrary string for organizer name as current user doesnot need to know about organizer name
                event = new Event(eventName, new Organizer("organizer", organizerId), description, eventId);




            }
        });
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