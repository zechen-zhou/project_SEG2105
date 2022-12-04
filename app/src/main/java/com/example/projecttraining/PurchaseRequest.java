package com.example.projecttraining;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.projecttraining.databinding.FragmentCookPurchaseRequestBinding;
import com.google.firebase.database.DatabaseReference;

public class PurchaseRequest extends Fragment {

    private FragmentCookPurchaseRequestBinding binding;

    DatabaseReference databaseRequest;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCookPurchaseRequestBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return  root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }
}
