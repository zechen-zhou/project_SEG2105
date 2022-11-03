package com.example.projecttraining;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ComplaintList extends ArrayAdapter<Complaint> {
    private Activity context;
    List<Complaint> complaints;

    public ComplaintList(Activity context, List<Complaint> complaints) {
        super(context, R.layout.layout_complaint_list, complaints);
        this.context = context;
        this.complaints = complaints;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_complaint_list, null, true);

        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);
        TextView textViewCookUser = (TextView) listViewItem.findViewById(R.id.textViewCookUser);
        TextView textViewDescription = (TextView) listViewItem.findViewById(R.id.textViewDescription);


        Complaint complaint = complaints.get(position);
        textViewName.setText(complaint.getComplaintName());
        textViewCookUser.setText(String.valueOf(complaint.getCookUser()));
        textViewDescription.setText(String.valueOf(complaint.getDescription()));
        return listViewItem;
    }
}
