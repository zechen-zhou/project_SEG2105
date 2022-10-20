package com.example.projecttraining;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Shows a login page
 */
public class Login extends Fragment {

    String[] items = {"Cook", "Client"};
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

        EditText email = binding.email;
        EditText password = binding.password;
        TextView register = binding.register;
        Button login = binding.signIn;
        AutoCompleteTextView autoCompleteTxt = binding.autoCompleteTxt;

        autoCompleteTxt.setOnItemClickListener((AdapterView<?> parent, View view1, int position, long id) -> {
                String item = parent.getItemAtPosition(position).toString();
                Toast.makeText(getActivity(), "Item: " + item, Toast.LENGTH_SHORT).show();
        });
        register.setOnClickListener(click -> {
            Navigation.findNavController(view).navigate(R.id.action_login_to_registerSelect);
        });

        login.setOnClickListener(click -> {
            String emailText = email.getText().toString();
            String passwordText = password.getText().toString();
            if (emailText.isEmpty() || passwordText.isEmpty()) {
                Toast.makeText(getActivity(), "Please enter your email or password", Toast.LENGTH_SHORT).show();
            } else {

                databaseReference.child("ClientUser").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.hasChild(emailText)) {
                           String getPassword = snapshot.child(emailText).child("password").getValue(String.class);
                           if (getPassword.equals(passwordText)) {

                               Toast.makeText(getActivity(), "Logged in! Welcome back", Toast.LENGTH_SHORT).show();
                               Navigation.findNavController(view).navigate(R.id.action_login_to_welcomeClient);

                           } else {

                               Toast.makeText(getActivity(), "wrong password" , Toast.LENGTH_SHORT).show();
                           }
                        } else {
                            Toast.makeText(getActivity(), "Wrong email or You haven't register yet!" , Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                databaseReference.child("CookUser").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.hasChild(emailText)) {
                            String getPassword = snapshot.child(emailText).child("password").getValue(String.class);
                            if (getPassword.equals(passwordText)) {

                                Toast.makeText(getActivity(), "Logged in! Welcome back", Toast.LENGTH_SHORT).show();
                                Navigation.findNavController(view).navigate(R.id.action_login_to_welcomeCook);

                            } else {

                                Toast.makeText(getActivity(), "wrong password1" , Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Wrong email or You haven't register yet!" , Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                databaseReference.child("Admin").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.hasChild(emailText)) {
                            String getPassword = snapshot.child(emailText).child("password").getValue(String.class);
                            if (getPassword.equals(passwordText)) {

                                Toast.makeText(getActivity(), "Logged in! Welcome back", Toast.LENGTH_SHORT).show();
                                Navigation.findNavController(view).navigate(R.id.action_login_to_welcomeCook);

                            } else {

                                Toast.makeText(getActivity(), "wrong password1" , Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Wrong email or You haven't register yet!" , Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

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
}