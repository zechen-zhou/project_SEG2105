package com.example.projecttraining;

import android.content.Context;
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
import com.example.projecttraining.user.Client;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

        Button welcomeClient = binding.submit;
        Button backToSignIn = binding.backToSignIn;

        EditText firstname = binding.firstNameInputEditText;
        EditText lastname = binding.lastNameInputEditText;
        EditText email = binding.registerEmailInputEditText;
        EditText password = binding.registerPasswordInputEditText;
        EditText address = binding.homeAddressEditText;
        EditText creditCardInfo = binding.creditCardInfoEditText;

        welcomeClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Client client;

                //get data from EditText from String variables
                String firstnameText = firstname.getText().toString();
                String lastnameText = lastname.getText().toString();
                String oddEmailText = email.getText().toString();
                String emailText = oddEmailText.replace('.', ',');
                String passwordText = password.getText().toString();
                String addressText = address.getText().toString();
                String creditCardInfoText = creditCardInfo.getText().toString();

                // check if user fill all the fields
                if (firstnameText.isEmpty() || lastnameText.isEmpty() || emailText.isEmpty() || passwordText.isEmpty() || addressText.isEmpty()) {
                    Toast.makeText(getActivity(), "Please fill up all fields", Toast.LENGTH_SHORT).show();
                } else {
                    Context context = getActivity();
                    client = new Client(firstnameText, lastnameText, emailText, passwordText, addressText, creditCardInfoText);

                    databaseReference.child("ClientUser").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(emailText)) {
                                Toast.makeText(context, "Email is registered", Toast.LENGTH_SHORT).show();
                            } else {
                                Navigation.findNavController(view).navigate(R.id.action_registerClient_to_login);
                                Toast.makeText(context, "Registration complete! Please login", Toast.LENGTH_SHORT).show();

                                databaseReference.child("ClientUser").child(emailText).child("firstname").setValue(client.getFirstName());
                                databaseReference.child("ClientUser").child(emailText).child("lastname").setValue(client.getLastName());
                                databaseReference.child("ClientUser").child(emailText).child("password").setValue(client.getPassword());
                                databaseReference.child("ClientUser").child(emailText).child("address").setValue(client.getAddress());
                                databaseReference.child("ClientUser").child(emailText).child("creditCardInfo").setValue(client.getCreditCardInfo());
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
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