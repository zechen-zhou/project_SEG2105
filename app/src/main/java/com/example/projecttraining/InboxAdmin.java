package com.example.projecttraining;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class InboxAdmin extends AppCompatActivity{
    EditText editTextClientUser;
    EditText editTextCookUser;
    EditText editTextDescription;
    Button buttonAddComplaint;
    Button backButton;

    ListView listViewComplaints;
    List<Complaint> complaints;

    DatabaseReference databaseComplaints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox_admin);

        databaseComplaints = FirebaseDatabase.getInstance().getReference("Complaints");

        //delete later
        editTextClientUser = (EditText) findViewById(R.id.editTextClientUser);
        editTextCookUser = (EditText) findViewById(R.id.editTextCookUser);
        editTextDescription = (EditText) findViewById(R.id.editTextDescription);
        buttonAddComplaint = (Button) findViewById(R.id.addButton);

        listViewComplaints = (ListView) findViewById(R.id.listViewComplaints);
        backButton = (Button) findViewById(R.id.backButton);

        complaints = new ArrayList<>();

        //delete later
        buttonAddComplaint.setOnClickListener(view -> addComplaint());

        listViewComplaints.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Complaint complaint = complaints.get(i);
                showDecisionDialog(complaint.getId(), complaint.getCookUser(), complaint.getClientUser(), complaint.getDescription());
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), WelcomeAdmin.class));
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        databaseComplaints.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                complaints.clear();

                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    Complaint complaint = postSnapshot.getValue(Complaint.class);
                    complaints.add(complaint);
                }
                ComplaintList complaintAdapter = new ComplaintList(InboxAdmin.this, complaints);
                listViewComplaints.setAdapter(complaintAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void showDecisionDialog(final String complaintId, String cookName, String clientName, String description) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.complaint_decision_dialog, null);
        dialogBuilder.setView(dialogView);

        final TextView editTextFromClient = dialogView.findViewById(R.id.fromClient);
        final TextView editTextDescription  = dialogView.findViewById(R.id.complaintDescription);
        final Spinner decisionDropdown = dialogView.findViewById(R.id.decisionSelect);
        final Button confirm = dialogView.findViewById(R.id.decisionConfirmButton);

        editTextFromClient.setText("From: "+clientName);
        editTextDescription.setText("Description: "+description);

        String[] decisionOptions = {"","dismiss", "suspend temporarily","suspend permanently"};
        ArrayAdapter<String> optionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, decisionOptions);
        decisionDropdown.setAdapter(optionAdapter);

        dialogBuilder.setTitle(cookName);
        final AlertDialog b = dialogBuilder.create();
        b.show();

        confirm.setOnClickListener(view -> {

            switch (decisionDropdown.getSelectedItemPosition()) {
                case 0:
                    Toast.makeText(this,"Please select an option first", Toast.LENGTH_LONG).show();
                    break;

                case 1: //dismiss
                    deleteComplaint(complaintId);
                    Toast.makeText(getApplicationContext(), "Dismissed: Complaint Deleted", Toast.LENGTH_LONG).show();
                    b.dismiss();
                    break;

                case 2: //temporary suspension
                    //TODO: temporary suspension implementation
                    //create a time selection that is only used when temporary suspension is chosen?
                    deleteComplaint(complaintId);
                    break;

                case 3: //permanent suspension
                    //TODO: permanent suspension implementation
                    //change cook user's status (add another data point in firebase)
                    break;
            }
        });
    }

    //TODO: change to update cook's status and delete complaint from inbox
    private void updateCookUser(String id, String name, String cookUser, String description) {

    }

    private void deleteComplaint(String id) {
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("Complaints").child(id);
        dR.removeValue();
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
            Toast.makeText(this, "Complaint Added", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_LONG).show();
        }

    }
}