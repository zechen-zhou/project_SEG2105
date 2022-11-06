package com.example.projecttraining;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.PopUpToBuilder;

import com.example.projecttraining.databinding.FragmentComplainCookBinding;
import com.example.projecttraining.user.Person;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Complain extends Fragment {

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

        Button submitText = binding.submitComplain;
        Button backToCook = binding.backToWelcomeCook;

        EditText complaint = binding.complaintxt;
        EditText cook = binding.cookName;
        String clientUser = person.getEmail();

        submitText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String description = complaint.getText().toString();

                if (description.isEmpty()) {
                    Toast.makeText(getActivity(),"Please fill up your complaint", Toast.LENGTH_SHORT).show();
                } else {
                    String id = databaseReference.push().getKey();
                    String cookUser = cook.getText().toString();
                    Complaint complaint = new Complaint(id, clientUser, cookUser, description);
                    databaseReference.child(id).setValue(complaint);
                }
            }
        });

        backToCook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_complain_to_welcome);
            }
        });
    }

}
