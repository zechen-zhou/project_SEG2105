package com.example.projecttraining;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.projecttraining.databinding.FragmentRegisterClientBinding;
import com.example.projecttraining.user.Client;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterClient extends Fragment {

    Login login;
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
        EditText emailInputEditText = binding.emailInputEditText;
        TextInputLayout emailInputLayout = binding.emailInputLayout;
        EditText passwordInputEditText = binding.passwordInputEditText;
        EditText address = binding.homeAddressEditText;
        EditText creditCardInfo = binding.creditCardInfoEditText;

        emailInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //remove emailInputLayout error when the text length is changed
                emailInputLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        welcomeClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Client client;

                //get data from EditText from String variables
                String firstnameText = firstname.getText().toString();
                String lastnameText = lastname.getText().toString();
                String oddEmailText = emailInputEditText.getText().toString();
                String emailText;
                String passwordText = passwordInputEditText.getText().toString();
                String addressText = address.getText().toString();
                String creditCardInfoText = creditCardInfo.getText().toString();

                // Email is empty
                if (oddEmailText.equals("")) {
                    emailInputLayout.setError(getString(R.string.email_empty_message));

                    // Remove the error icon, so it will keep using the "clear_text" mode
                    emailInputLayout.setErrorIconDrawable(null);
                } else if (!login.isValidEmail(oddEmailText)) { // Email is not valid
                    emailInputLayout.setError(getString(R.string.email_error_message));

                    // Remove the error icon, so it will keep using the "clear_text" mode
                    emailInputLayout.setErrorIconDrawable(null);
                }

                // check if user fill all the fields
                if (firstnameText.isEmpty() || lastnameText.isEmpty() || !login.isValidEmail(oddEmailText) || passwordText.isEmpty() || addressText.isEmpty()) {
                    Toast.makeText(getActivity(), "Please fill up all fields", Toast.LENGTH_SHORT).show();
                } else {
                    emailText = oddEmailText.replace('.', ',');
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