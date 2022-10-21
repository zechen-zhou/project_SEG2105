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

import com.example.projecttraining.databinding.FragmentWelcomeClientBinding;
import com.example.projecttraining.user.Client;
import com.example.projecttraining.user.Person;

public class WelcomeClient extends Fragment {

    private FragmentWelcomeClientBinding binding;
    private Client currentUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentWelcomeClientBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = this.getArguments();
        if (bundle!=null) {
            currentUser = (Client) bundle.get("clientUser");
        }

        TextView firstName = binding.firstName;
        TextView lastName = binding.lastName;
        TextView email = binding.emailAddress;
        TextView address = binding.homeAddress;
        TextView card = binding.creditInfo;

        Button logout = binding.logout;

        //display corresponding Cook information on screen
        firstName.setText(currentUser.getFirstName());
        lastName.setText(currentUser.getLastName());
        String convertEmail = currentUser.getEmail();
        String theEmail = convertEmail.replace(',','.');
        email.setText(theEmail);
        address.setText(currentUser.getAddress());
        card.setText(currentUser.getCreditCardInfo());

        logout.setOnClickListener(click -> {
            FragmentManager fragmentManager = getParentFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.my_nav_host_fragment, new Login(), null).commit();
        });
    }
}