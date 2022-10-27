package com.example.projecttraining;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.projecttraining.databinding.FragmentWelcomeCookBinding;
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
            currentUser = (Cook) bundle.getParcelable("cookUser");
        }

        TextView firstName = binding.firstName;
        TextView lastName = binding.lastName;
        TextView email = binding.emailAddress;
        TextView address = binding.homeAddress;
        TextView description = binding.shortDescription;

        Button logout = binding.logout;

        //display corresponding Cook information on screen
        firstName.setText(currentUser.getFirstName());
        lastName.setText(currentUser.getLastName());
        String convertEmail = currentUser.getEmail();
        String theEmail = convertEmail.replace(',','.');
        email.setText(theEmail);
        address.setText(currentUser.getAddress());
        description.setText(currentUser.getCookDescription());

        logout.setOnClickListener(click -> {
            Navigation.findNavController(view).navigate(R.id.action_welcomeCook_to_login);
        });
    }
}