package com.example.projecttraining;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.projecttraining.databinding.FragmentWelcomeCookBinding;
import com.example.projecttraining.user.Client;
import com.example.projecttraining.user.Cook;

public class WelcomeCook extends Fragment {

    private FragmentWelcomeCookBinding binding;
    private Cook currentUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentWelcomeCookBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = this.getArguments();
        if (bundle!=null) {
            currentUser = (Cook) bundle.get("cookUser");
        }

        TextView firstName = binding.firstName;
        TextView lastName = binding.lastName;
        TextView email = binding.emailAddress;
        TextView address = binding.homeAddress;
        TextView description = binding.shortDescription;

        Button logout = binding.logout;

        //TODO: get intent of class Person from login page (to be implemented)
        //display corresponding Cook information on screen
        firstName.setText(currentUser.getFirstName());
        lastName.setText(currentUser.getLastName());
        email.setText(currentUser.getEmail());
        address.setText(currentUser.getAddress());
        description.setText(currentUser.getCookDescription());

        logout.setOnClickListener(click -> {
            FragmentManager fragmentManager = getParentFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.my_nav_host_fragment, new Login(), null).commit();
        });
    }
}