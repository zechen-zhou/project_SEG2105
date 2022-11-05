package com.example.projecttraining;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projecttraining.databinding.FragmentInboxAdminBinding;
import com.example.projecttraining.databinding.FragmentWelcomeCookBinding;
import com.example.projecttraining.user.Administrator;
import com.example.projecttraining.user.Cook;
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

    EditText editTextClientUser;
    EditText editTextCookUser;
    EditText editTextDescription;
    Button buttonAddComplaint;

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

        //delete later
        editTextClientUser = binding.editTextClientUser;
        editTextCookUser = binding.editTextCookUser;
        editTextDescription = binding.editTextDescription;
        buttonAddComplaint = binding.addButton;

        listViewComplaints = binding.listViewComplaints;

        complaints = new ArrayList<>();

        //delete later
        buttonAddComplaint.setOnClickListener(click->{
            addComplaint();
        });

        listViewComplaints.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Complaint complaint = complaints.get(i);
                showDecisionDialog(complaint.getId(), complaint.getCookUser(), complaint.getClientUser(), complaint.getDescription());
            }
        });

        databaseComplaints.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                complaints.clear();

                for(DataSnapshot postSnapchot : dataSnapshot.getChildren()){
                    Complaint complaint = postSnapchot.getValue(Complaint.class);
                    complaints.add(complaint);
                }
                ComplaintList complaintAdapter = new ComplaintList(getActivity(), complaints);
                listViewComplaints.setAdapter(complaintAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    //TODO: delete addComplaint functionality and corresponding UI elements in xml file (at the end)
    //used for testing purposes (will delete after database has a few complaints)
    private void addComplaint() {
        String clientUser = editTextClientUser.getText().toString().trim();
        String cookUser = editTextCookUser.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();

        if(!TextUtils.isEmpty(clientUser)){
            String id = databaseComplaints.push().getKey();
            Complaint complaint = new Complaint(id, clientUser, cookUser, description);
            databaseComplaints.child(id).setValue(complaint);
            editTextClientUser.setText("");
            editTextCookUser.setText("");
            editTextDescription.setText("");
            Toast.makeText(getActivity(), "Complaint Added", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(getActivity(), "Please enter a name", Toast.LENGTH_LONG).show();
        }

    }

    private void showDecisionDialog(final String complaintId, String cookName, String clientName, String description) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.complaint_decision_dialog, null);
        dialogBuilder.setView(dialogView);

        final TextView editTextFromClient = dialogView.findViewById(R.id.fromClient);
        final TextView editTextDescription  = dialogView.findViewById(R.id.complaintDescription);
        final Spinner decisionDropdown = dialogView.findViewById(R.id.decisionSelect);
        final Button confirm = dialogView.findViewById(R.id.decisionConfirmButton);
        final EditText dateText = dialogView.findViewById(R.id.editDate);

        editTextFromClient.setText("From: "+clientName);
        editTextDescription.setText("Description: "+description);

        String[] decisionOptions = {"","dismiss", "suspend temporarily","suspend permanently"};
        ArrayAdapter<String> optionAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, decisionOptions);
        decisionDropdown.setAdapter(optionAdapter);

        dialogBuilder.setTitle(cookName);
        final AlertDialog b = dialogBuilder.create();
        b.show();

        confirm.setOnClickListener(view -> {

            switch (decisionDropdown.getSelectedItemPosition()) {
                case 0:
                    Toast.makeText(getActivity(),"Please select an option first", Toast.LENGTH_LONG).show();
                    break;

                case 1: //dismiss
                    deleteComplaint(complaintId);
                    Toast.makeText(getActivity(), "Dismissed: Complaint Deleted", Toast.LENGTH_LONG).show();
                    b.dismiss();
                    break;

                case 2: //temporary suspension
                    //TODO: temporary suspension implementation
                    String endDate = dateText.getText().toString();

                    if (endDate.equals("")) {
                        Toast.makeText(getActivity(), "Please enter an end date", Toast.LENGTH_LONG).show();

                    } else if (!endDate.matches("^\\d{4}-\\d{2}-\\d{2}$")) { //regex to check correct format -- can be improved, maybe later?
                        Toast.makeText(getActivity(), "Please use the correct format (YYYY-MM-DD)", Toast.LENGTH_LONG).show();

                    } else {
                        //change cook user's status + add end date
                        deleteComplaint(complaintId);
                    }

                    break;

                case 3: //permanent suspension
                    //TODO: permanent suspension implementation
                    //change cook user's status (add another data point in firebase)
                    break;
            }
        });
    }

    private void deleteComplaint(String id) {
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("Complaints").child(id);
        dR.removeValue();
    }
}