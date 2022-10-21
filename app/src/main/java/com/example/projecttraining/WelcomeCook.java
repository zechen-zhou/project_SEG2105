package com.example.projecttraining;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.projecttraining.databinding.FragmentWelcomeCookBinding;

public class WelcomeCook extends Fragment {

    private FragmentWelcomeCookBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentWelcomeCookBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView firstName = binding.firstName;
        TextView lastName = binding.lastName;
        TextView email = binding.emailAddress;
        TextView address = binding.homeAddress;
        TextView description = binding.shortDescription;

        //get intent of class Person from login page (to be implemented)

        //display corresponding Cook information on screen
        firstName.setText(String.valueOf("placeholder"));
        lastName.setText(String.valueOf("placeholder"));
        email.setText(String.valueOf("placeholder"));
        address.setText(String.valueOf("placeholder"));
        description.setText(String.valueOf("placeholder"));

    }
}