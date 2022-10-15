package com.example.projecttraining;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.projecttraining.databinding.FragmentRegisterClientBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterClient extends Fragment {

    private FragmentRegisterClientBinding binding;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://mealer-dd302-default-rtdb.firebaseio.com/");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRegisterClientBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button welcomeCook = binding.submit;
        Button backToSignIn = binding.backToSignIn;

        EditText firstname = binding.firstName;
        EditText lastname = binding.lastName;
        EditText email = binding.email;
        EditText password = binding.password;
        EditText address = binding.homeAddress;
        EditText description = binding.ClientDescription;

        welcomeCook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //get data from EditText from String variables
                String firstnameText = firstname.getText().toString();
                String lastnameText = lastname.getText().toString();
                String emailText = email.getText().toString();
                String passwordText = password.getText().toString();
                String addressText = address.getText().toString();
                String descriptionText = description.getText().toString();

                // check if user fill all the fields
                if (firstnameText.isEmpty() || lastnameText.isEmpty() || emailText.isEmpty() || passwordText.isEmpty() || addressText.isEmpty()) {
                    Toast.makeText(getActivity(), "Please fill up all fields", Toast.LENGTH_SHORT).show();
                } else {
                    Navigation.findNavController(view).navigate(R.id.action_registerCook_to_welcomeCook);
                }

                databaseReference.child("user").child(emailText).child("firstname").setValue(firstnameText);
                databaseReference.child("user").child(emailText).child("lastname").setValue(lastnameText);
                databaseReference.child("user").child(emailText).child("password").setValue(passwordText);
                databaseReference.child("user").child(emailText).child("address").setValue(addressText);
                databaseReference.child("user").child(emailText).child("description").setValue(descriptionText);

            }
        });

        backToSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate((R.id.action_registerClient_to_login));
            }
        });
    }
}