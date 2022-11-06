package com.example.projecttraining;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.projecttraining.databinding.FragmentWelcomeAdminBinding;
import com.example.projecttraining.user.Administrator;

public class WelcomeAdmin extends Fragment {

    private FragmentWelcomeAdminBinding binding;
    private Administrator currentUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentWelcomeAdminBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = this.getArguments();
        if (bundle!=null) {
            currentUser = (Administrator) bundle.getParcelable("adminUser");
        }

        Button logout = binding.logout;
        Button inbox = binding.INBOX;

        logout.setOnClickListener(click -> {
            Navigation.findNavController(view).navigate(R.id.action_welcomeAdmin_to_login);
        });

        inbox.setOnClickListener(click -> {
            Navigation.findNavController(view).navigate(R.id.action_welcomeAdmin_to_inbox_Admin);
        });
    }
}