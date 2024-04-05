package com.example.qr_check_in.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {
    private final MutableLiveData<String> eventName = new MutableLiveData<>();
    private final MutableLiveData<String> eventDescription = new MutableLiveData<>();
    private final MutableLiveData<String> eventId = new MutableLiveData<>();

    public HomeViewModel() {
        // Example initial data
        eventName.setValue("Community Event");
        eventDescription.setValue("This is a description of the event.");
    }

    public LiveData<String> getEventName() {
        return eventName;
    }

    public LiveData<String> getEventDescription() {
        return eventDescription;
    }

    public LiveData<String> getEventId() { // Getter for eventId
        return eventId;
    }

    public void setEventId(String eventId) { // Setter for eventId
        this.eventId.setValue(eventId);
    }
}
