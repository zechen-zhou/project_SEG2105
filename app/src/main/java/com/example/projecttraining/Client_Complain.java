package com.example.projecttraining;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.projecttraining.databinding.FragmentComplainCookBinding;
import com.example.projecttraining.user.Client;
import com.example.projecttraining.user.Person;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Client_Complain extends Fragment {

    Person person;
    private FragmentComplainCookBinding binding;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl(("https://mealer-dd302-default-rtdb.firebaseio.com/"));

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentComplainCookBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    public void onViewCreated(@Nullable View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            person = (Client) bundle.getParcelable("clientUser");
        }

        Button submitText = binding.submitComplain;
        Button backToCook = binding.backToWelcomeCook;

        EditText complaint = binding.complaintxt;
        EditText cook = binding.cookName;
        String clientUser = person.getEmail();
        String cookUser = person.getEmail();

        submitText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String description = complaint.getText().toString();
                String cookName = cook.getText().toString();

                if (description.isEmpty()) {
                    Toast.makeText(getActivity(),"Please fill up your complaint", Toast.LENGTH_SHORT).show();
                }
                if(cookName.isEmpty()) {
                    Toast.makeText(getActivity(), "Please fill up the cook's name", Toast.LENGTH_SHORT).show();
                }

                databaseReference.child("CookUser").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild(cookUser)) {
                            String id = databaseReference.push().getKey();
                            String cookUser = cook.getText().toString();
                            Complaint complaint = new Complaint(id, clientUser, cookUser, description);
                            databaseReference.child(id).setValue(complaint);
                        } else {
                            Toast.makeText(getActivity(),"The cook isn't exist. Please enter the valid cook's name", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        backToCook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_complain_to_welcome, bundle);
            }
        });
    }

}
