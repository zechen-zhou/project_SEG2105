package com.example.projecttraining;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.projecttraining.databinding.FragmentInboxAdminBinding;
import com.example.projecttraining.user.Administrator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class Inbox_Admin extends Fragment {
    private FragmentInboxAdminBinding binding;
    private Administrator currentAdmin;
    ListView listViewComplaints;
    List<Complaint> complaints;

    DatabaseReference databaseComplaints;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentInboxAdminBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }


    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        databaseComplaints = FirebaseDatabase.getInstance().getReference("Complaints");
        listViewComplaints = binding.listViewComplaints;

        complaints = new ArrayList<>();


        listViewComplaints.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Complaint complaint = complaints.get(i);
                String[] complaintInfo = {complaint.getId(), complaint.getCookUser(), complaint.getClientUser(), complaint.getDescription()};

                Bundle bundle = new Bundle();
                bundle.putStringArray("complaint", complaintInfo);

                Navigation.findNavController(view).navigate(R.id.action_inbox_Admin_to_complaintDecision, bundle);
            }
        });

        databaseComplaints.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                complaints.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    boolean isRead = postSnapshot.child("read").getValue(Boolean.class);
                    Complaint complaint = postSnapshot.getValue(Complaint.class);
                    complaint.setReadStatus(isRead);

                    complaint.addComplaint(complaints, isRead, complaint);
                }
                Activity activity = getActivity();
                if (activity != null) {
                    ComplaintList complaintAdapter = new ComplaintList(activity, complaints);
                    listViewComplaints.setAdapter(complaintAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }
}