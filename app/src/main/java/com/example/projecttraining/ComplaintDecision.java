package com.example.projecttraining;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projecttraining.databinding.FragmentComplaintDecisionBinding;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class ComplaintDecision extends Fragment {

    private FragmentComplaintDecisionBinding binding;
    private String[] complaintInfo;

    private DatePickerDialog.OnDateSetListener dateSetListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreate(savedInstanceState);

        binding = FragmentComplaintDecisionBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    public void editCookStatus(String cookName, int status) {
        FirebaseDatabase.getInstance().getReference("CookUser").child(cookName).child("status").setValue(status);
    }

    public void editComplaintRead(String id, boolean status) {
        FirebaseDatabase.getInstance().getReference("Complaints").child(id).child("read").setValue(status);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            complaintInfo = bundle.getStringArray("complaint");
        }

        //layout displays corresponding complaint information
        TextView cookTitle = binding.complaintTitle;
        TextView fromClient = binding.fromClient;
        TextView description = binding.complaintDescription;

        cookTitle.setText(complaintInfo[1].replace(",", "."));
        fromClient.setText("From: " + complaintInfo[2].replace(",", "."));
        description.setText("Description: " + complaintInfo[3]);

        //components used for entering decision
        Button confirm = binding.decisionConfirmButton;
        Spinner decisionDropdown = binding.decisionSelect;
        TextView dateText = binding.endDate;
        Button setDate = binding.setDateButton;

        //dropdown options
        String[] decisionOptions = {"","dismiss", "suspend temporarily","suspend permanently"};
        ArrayAdapter<String> optionAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, decisionOptions);
        decisionDropdown.setAdapter(optionAdapter);

        //setting up date picker dialog
        setDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dateDialog = new DatePickerDialog(
                        getActivity(),
                        android.R.style.Theme_DeviceDefault_Dialog_MinWidth,
                        dateSetListener,
                        year, month, day);
                dateDialog.show();
            }
        });

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                String date = year + "-" + (month++) + "-" + day;
                dateText.setText(date);
            }
        };

        //confirm decision
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (decisionDropdown.getSelectedItemPosition()) {
                    case 0:
                        Toast.makeText(getActivity(), "Please select an option first", Toast.LENGTH_LONG).show();
                        break;

                    case 1: //dismiss
                        //changes complaint read element to true
                        editComplaintRead(complaintInfo[0], true);
                        Toast.makeText(getActivity(), "Dismissed", Toast.LENGTH_LONG).show();
                        break;

                    case 2: //temporary suspension
                        String endDate = dateText.getText().toString();

                        if (endDate.equals("YYYY-MM-DD")) {
                            Toast.makeText(getActivity(), "Please enter an end date", Toast.LENGTH_LONG).show();

                        } else {
                            //change cook user's status to 1 (temp)
                            editCookStatus(complaintInfo[1], 1);
                            //changes complaint suspensionDate element to endDate
                            FirebaseDatabase.getInstance().getReference("Complaints").child(complaintInfo[0]).child("suspensionDate").setValue(endDate);
                            //changes complaint read element to true
                            editComplaintRead(complaintInfo[0], true);
                            Toast.makeText(getActivity(), "Successful. Suspended temporarily", Toast.LENGTH_LONG).show();
                        }
                        break;

                    case 3: //permanent suspension
                        //change cook user's status to 2 (permanent)
                        editCookStatus(complaintInfo[1], 2);
                        //change complaint read element to true
                        editComplaintRead(complaintInfo[0], true);
                        Toast.makeText(getActivity(), "Successful. Suspended permanently", Toast.LENGTH_LONG).show();
                        break;
                };
            }
        });

    }
}