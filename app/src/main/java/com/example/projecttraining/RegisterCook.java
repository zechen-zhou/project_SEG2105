package com.example.projecttraining;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.projecttraining.databinding.FragmentRegisterCookBinding;
import com.example.projecttraining.user.Cook;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;


public class RegisterCook extends Fragment {

    private FragmentRegisterCookBinding binding;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://mealer-dd302-default-rtdb.firebaseio.com/");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRegisterCookBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button welcomeCook = binding.submit;
        Button backToSignIn = binding.backToSignIn;

        EditText firstname = binding.firstName;
        EditText lastname = binding.lastName;
        EditText email = binding.email;
        EditText password = binding.password;
        EditText address = binding.homeAddress;
        EditText description = binding.cookDescription;

        welcomeCook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cook cook;

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
                    cook = new Cook(firstnameText, lastnameText, emailText, passwordText, addressText, descriptionText);
                    Navigation.findNavController(view).navigate(R.id.action_registerCook_to_welcomeCook);

                    databaseReference.child("CookUser").child(emailText).child("firstname").setValue(cook.getFirstName());
                    databaseReference.child("CookUser").child(emailText).child("lastname").setValue(cook.getLastName());
                    databaseReference.child("CookUser").child(emailText).child("password").setValue(cook.getPassword());
                    databaseReference.child("CookUser").child(emailText).child("address").setValue(cook.getAddress());
                    databaseReference.child("CookUser").child(emailText).child("description").setValue(cook.getCookDescription());
                }
            }
        });

        backToSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate((R.id.action_registerCook_to_login));
            }
        });
    }
}

