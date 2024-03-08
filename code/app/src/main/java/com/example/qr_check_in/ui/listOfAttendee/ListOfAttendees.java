package com.example.qr_check_in.ui.listOfAttendee;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.qr_check_in.R;
import com.example.qr_check_in.databinding.FragmentGalleryBinding;
import com.example.qr_check_in.databinding.FragmentListOfAttendeesBinding;
import com.example.qr_check_in.ui.gallery.GalleryViewModel;

public class ListOfAttendees extends Fragment {

    private ListOfAttendeesViewModel mViewModel;
    private FragmentListOfAttendeesBinding binding;

    public static ListOfAttendees newInstance() {
        return new ListOfAttendees();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_list_of_attendees, container, false);

        ListOfAttendeesViewModel galleryViewModel = new ViewModelProvider(this).get(ListOfAttendeesViewModel.class);

//        binding = FragmentListOfAttendeesBinding.inflate(inflater, container, false);
        View root = inflater.inflate(R.layout.fragment_list_of_attendees, container, false);

//        final TextView textView = binding.textGallery;
//        galleryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ListOfAttendeesViewModel.class);
        // TODO: Use the ViewModel
    }

}