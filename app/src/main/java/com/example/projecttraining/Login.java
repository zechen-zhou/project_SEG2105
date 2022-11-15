package com.example.projecttraining;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projecttraining.databinding.FragmentLoginBinding;
import com.example.projecttraining.user.*;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Shows a login page
 */
public class Login extends Fragment {

    String[] items = {"Cook", "Client", "Admin"};
    ArrayAdapter<String> adapterItems;

    private FragmentLoginBinding binding;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://mealer-dd302-default-rtdb.firebaseio.com/");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextInputLayout userTypeInputLayout = binding.Iama;
        AutoCompleteTextView userTypeTextView = binding.autoCompleteTxt;
        EditText emailInputEditText = binding.emailInputEditText;
        TextInputLayout emailInputLayout = binding.emailInputLayout;
        EditText passwordInputEditText = binding.passwordInputEditText;
        TextInputLayout passwordInputLayout = binding.passwordInputLayout;
        TextView register = binding.register;
        Button login = binding.signIn;
        AutoCompleteTextView autoCompleteTxt = binding.autoCompleteTxt;

        final String[] item = new String[1];

        autoCompleteTxt.setOnItemClickListener((AdapterView<?> parent, View view1, int position, long id) -> {
            item[0] = parent.getItemAtPosition(position).toString();
            Toast.makeText(getActivity(), "Item: " + item[0], Toast.LENGTH_SHORT).show();
        });
        register.setOnClickListener(click -> {
            Navigation.findNavController(view).navigate(R.id.action_login_to_registerSelect);
        });

        userTypeTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //remove userTypeInputLayout error when the text length is changed
                userTypeInputLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        passwordInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //remove passwordInputLayout error when the text length is changed
                passwordInputLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

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

        login.setOnClickListener(click -> {
            String oddEmailText = emailInputEditText.getText().toString();
            String emailText;
            String passwordText = passwordInputEditText.getText().toString();

            // User type is not selected
            if (item[0] == null) {
                userTypeInputLayout.setError(getString(R.string.user_type_empty_message));
            }

            // Password is empty
            if (passwordText.equals("")) {
                passwordInputLayout.setError(getString(R.string.password_empty_message));

                // Remove the error icon, so it will keep using the "password_toggle" mode
                passwordInputLayout.setErrorIconDrawable(null);
            }

            // Email is empty
            if (oddEmailText.equals("")) {
                emailInputLayout.setError(getString(R.string.email_empty_message));

                // Remove the error icon, so it will keep using the "clear_text" mode
                emailInputLayout.setErrorIconDrawable(null);
            } else if (!isValidEmail(oddEmailText)) { // Email is not valid
                emailInputLayout.setError(getString(R.string.email_error_message));

                // Remove the error icon, so it will keep using the "clear_text" mode
                emailInputLayout.setErrorIconDrawable(null);
            }

            // User type is selected, email is valid and password is not empty
            if (item[0] != null && isValidEmail(oddEmailText) && !passwordText.equals("")) {
                emailText = oddEmailText.replace('.', ',');

//                if (item[0] == null) {
//                    Toast.makeText(getActivity(), "Please select the type of user", Toast.LENGTH_LONG).show();
//                } else {
                if (item[0].equals("Client")) {
                    databaseReference.child("ClientUser").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if (snapshot.hasChild(emailText)) {
                                String getPassword = snapshot.child(emailText).child("password").getValue(String.class);
                                if (getPassword.equals(passwordText)) {

                                    String firstName = snapshot.child(emailText).child("firstname").getValue(String.class);
                                    String lastName = snapshot.child(emailText).child("lastname").getValue(String.class);
                                    String password = snapshot.child(emailText).child("password").getValue(String.class);
                                    String address = snapshot.child(emailText).child("address").getValue(String.class);
                                    String cardInfo = snapshot.child(emailText).child("creditCardInfo").getValue(String.class);

                                    Client user = new Client(firstName, lastName, emailText, password, address, cardInfo);

                                    Bundle bundle = new Bundle();
                                    bundle.putParcelable("clientUser", user);

                                    Navigation.findNavController(view).navigate(R.id.action_login_to_welcomeClient, bundle);

                                    Toast.makeText(getActivity(), "Logged in! Welcome back", Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(getActivity(), "Wrong password", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getActivity(), "Wrong email or You haven't register yet!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }

                    });
                } else if (item[0].equals("Cook")) {
                    databaseReference.child("CookUser").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if (snapshot.hasChild(emailText)) {
                                String getPassword = snapshot.child(emailText).child("password").getValue(String.class);
                                if (getPassword.equals(passwordText)) {

                                    String firstName = snapshot.child(emailText).child("firstname").getValue(String.class);
                                    String lastName = snapshot.child(emailText).child("lastname").getValue(String.class);
                                    String password = snapshot.child(emailText).child("password").getValue(String.class);
                                    String address = snapshot.child(emailText).child("address").getValue(String.class);
                                    String description = snapshot.child(emailText).child("description").getValue(String.class);

                                    Cook user = new Cook(firstName, lastName, emailText, password, address, description);

                                    Bundle bundle = new Bundle();
                                    bundle.putParcelable("cookUser", user);

                                    Navigation.findNavController(view).navigate(R.id.action_login_to_welcomeCook, bundle);

                                    Toast.makeText(getActivity(), "Logged in! Welcome back", Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(getActivity(), "Wrong password", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getActivity(), "Wrong email or You haven't register yet!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } else if (item[0].equals("Admin")) {
                    databaseReference.child("Admin").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if (snapshot.hasChild(emailText)) {
                                String getPassword = snapshot.child(emailText).child("password").getValue(String.class);
                                if (getPassword.equals(passwordText)) {

                                    String firstName = "Admin";
                                    String lastName = "name";
                                    String password = snapshot.child(emailText).child("password").getValue(String.class);
                                    String address = "123 street";

                                    Administrator user = new Administrator(firstName, lastName, emailText, password, address);

                                    Bundle bundle = new Bundle();
                                    bundle.putParcelable("adminUser", user);

                                    Navigation.findNavController(view).navigate(R.id.action_login_to_welcomeAdmin, bundle);

                                    Toast.makeText(getActivity(), "Logged in! Welcome back", Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(getActivity(), "Wrong password", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getActivity(), "Wrong email or You haven't register yet!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
//            }

        });
    }

    @Override
    public void onResume() {
        super.onResume();

        AutoCompleteTextView autoCompleteTxt = binding.autoCompleteTxt;

        // Set adapter in onResume() so that the dropdownMenu can show all items even if we navigate
        // to other fragments and coming back
        adapterItems = new ArrayAdapter<String>(getActivity(), R.layout.list_item, items);
        autoCompleteTxt.setAdapter(adapterItems);
    }

    /**
     * Check if the email address is valid.
     *
     * @param email an email address
     * @return true if the email address is valid, false otherwise.
     */
    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return email.matches(emailPattern);
    }
}