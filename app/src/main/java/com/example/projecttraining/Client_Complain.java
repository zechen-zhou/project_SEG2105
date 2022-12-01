package com.example.projecttraining;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.projecttraining.databinding.FragmentComplainCookBinding;
import com.example.projecttraining.user.Client;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Client_Complain extends Fragment {

    Client currentUser;
    String cookUser = "";
    String mealName = "";
    private FragmentComplainCookBinding binding;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Complaints");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentComplainCookBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    public void onViewCreated(@Nullable View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //get current user and cook they will complain about
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            currentUser = (Client) bundle.getParcelable("clientUser");
            cookUser = bundle.getString("complainCook");
            mealName = bundle.getString("complainMeal");
        }

        Button submitText = binding.submitComplain;

        EditText complaint = binding.complaintxt;
        TextView meal = binding.mealName;
        TextView cook = binding.cookName;
        String clientUser = currentUser.getEmail();

        meal.setText(mealName);
        cook.setText("By: "+cookUser);

        submitText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String description = complaint.getText().toString();

                if (description.isEmpty()) {
                    Toast.makeText(getActivity(),"Please fill up your complaint", Toast.LENGTH_SHORT).show();
                }

                String id = databaseReference.push().getKey();
                Complaint complaint = new Complaint(id, clientUser, cookUser, description);
                databaseReference.child(id).setValue(complaint);

            }
        });

    }

}
