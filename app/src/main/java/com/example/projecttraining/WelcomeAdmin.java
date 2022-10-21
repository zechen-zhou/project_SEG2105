package com.example.projecttraining;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.projecttraining.databinding.FragmentWelcomeAdminBinding;

public class WelcomeAdmin extends Fragment {

    private FragmentWelcomeAdminBinding binding;

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

        Button logout = binding.logout;

        logout.setOnClickListener(click -> {
            Navigation.findNavController(view).navigate(R.id.action_welcomeAdmin_to_login);
        });
    }
}