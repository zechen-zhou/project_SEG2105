package com.example.projecttraining;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class InboxAdmin extends AppCompatActivity{
    EditText editTextName;
    EditText editTextCookUser;
    EditText editTextDescription;
    Button buttonAddComplaint;
    ListView listViewComplaints;
    List<Complaint> complaints;

    DatabaseReference databaseComplaints;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_inbox_admin);

        databaseComplaints = FirebaseDatabase.getInstance().getReference("complaints");

        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextCookUser = (EditText) findViewById(R.id.editTextCookUser);
        editTextDescription = (EditText) findViewById(R.id.editTextDescription);
        listViewComplaints = (ListView) findViewById(R.id.listViewComplaints);
        buttonAddComplaint = (Button) findViewById(R.id.addButton);

        complaints = new ArrayList<>();

        //adding an onclicklistener to button
        buttonAddComplaint.setOnClickListener(view -> addComplaint());

        listViewComplaints.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Complaint complaint = complaints.get(i);
                showUpdateDeleteDialog(complaint.getId(), complaint.getComplaintName());
                return true;
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


    private void showUpdateDeleteDialog(final String complaintId, String complaintName) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextName = (EditText) dialogView.findViewById(R.id.editTextName);
        final EditText editTextCookUser  = (EditText) dialogView.findViewById(R.id.editTextCookUser);
        final EditText editTextDescription  = (EditText) dialogView.findViewById(R.id.editTextDescription);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonUpdateComplaint);
        final Button buttonDelete = (Button) dialogView.findViewById(R.id.buttonDeleteComplaint);

        dialogBuilder.setTitle(complaintName);
        final AlertDialog b = dialogBuilder.create();
        b.show();

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextName.getText().toString().trim();
                String cookUser = editTextCookUser.getText().toString().trim();
                String description = editTextDescription.getText().toString().trim();
                if (!TextUtils.isEmpty(name)) {
                    updateComplaint(complaintId, name, cookUser, description);
                    b.dismiss();
                }
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteComplaint(complaintId);
                b.dismiss();
            }
        });
    }

    private void updateComplaint(String id, String name, String cookUser, String description) {
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("complaints").child(id);
        Complaint complaint = new Complaint(id, name, cookUser, description);
        dR.setValue(complaint);

        Toast.makeText(getApplicationContext(), "Complaint Updated", Toast.LENGTH_LONG).show();

    }

    private void deleteComplaint(String id) {
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("complaints").child(id);
        dR.removeValue();
        Toast.makeText(getApplicationContext(), "Complaint Deleted", Toast.LENGTH_LONG).show();
    }

    private void addComplaint() {
        String name = editTextName.getText().toString().trim();
        String cookUser = editTextName.getText().toString().trim();
        String description = editTextName.getText().toString().trim();
        if(!TextUtils.isEmpty(name)){
            String id = databaseComplaints.push().getKey();
            Complaint complaint = new Complaint(id, name, cookUser, description);
            databaseComplaints.child(id).setValue(complaint);
            editTextName.setText("");
            editTextCookUser.setText("");
            editTextDescription.setText("");
            Toast.makeText(this, "Complaint Added", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_LONG).show();
        }

    }
}