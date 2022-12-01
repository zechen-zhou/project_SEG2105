package com.example.projecttraining;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.projecttraining.databinding.FragmentWelcomeClientBinding;
import com.example.projecttraining.user.Client;

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
        if (bundle != null) {
            currentUser = (Client) bundle.getParcelable("clientUser");
        }

        TextView firstName = binding.firstName;
        TextView lastName = binding.lastName;
        TextView email = binding.emailAddress;
        TextView address = binding.homeAddress;
        TextView card = binding.creditInfo;

        Button logout = binding.logout;
        Button order = binding.ordernow;
        Button history = binding.orderhistory;

        firstName.setText(currentUser.getFirstName());
        lastName.setText(currentUser.getLastName());
        String convertEmail = currentUser.getEmail();
        String theEmail = convertEmail.replace(',', '.');
        email.setText(theEmail);
        address.setText(currentUser.getAddress());
        card.setText(currentUser.getCreditCardInfo());

        order.setOnClickListener(click -> {
            Navigation.findNavController(view).navigate(R.id.action_welcomeClient_to_offeredMeals);
        });

        logout.setOnClickListener(click -> {
            Navigation.findNavController(view).navigate(R.id.action_welcomeClient_to_login);
        });

        history.setOnClickListener(click -> {
            Navigation.findNavController(view).navigate(R.id.action_welcomeClient_to_client_OrderHistory, bundle);
        });
    }
}