package com.example.projecttraining;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.projecttraining.databinding.FragmentCookPurchaseRequestBinding;
import com.example.projecttraining.user.Cook;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PurchaseRequest extends Fragment {

    private FragmentCookPurchaseRequestBinding binding;
    private ListView requestListView;
    private List<Request> requestList;
    private RequestList Adapter;
    private Cook cookUser;

    DatabaseReference databaseRequest;
    DatabaseReference databaseMeal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCookPurchaseRequestBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return  root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        requestListView = binding.requestListView;
        requestList = new ArrayList<>();

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            cookUser = (Cook) bundle.getParcelable("cookUser");
        }

        databaseRequest = FirebaseDatabase.getInstance().getReference("Request");
        databaseMeal = FirebaseDatabase.getInstance().getReference("Meals");

        //traverse through all requests to find requests for this cook
        databaseRequest.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                requestList.clear();

                for(DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Request request = postSnapshot.getValue(Request.class);

                    //if the request is for this cook --> add to request list
                    if (request.cookID.equals(cookUser.getEmail()) && request.getRequest_type()==Request_type.PENDING) {
                        requestList.add(request);
                    }

                }

                Activity activity = getActivity();
                if (activity != null) {
                    Adapter = new RequestList(getActivity(), requestList);
                    requestListView.setAdapter(Adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        requestListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Request request = requestList.get(i);
                reviewRequest(request);

            }
        });
    }

    private void reviewRequest(Request request) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.layout_request_change, null);
        dialogBuilder.setView(dialogView);

        Button accept = (Button) dialogView.findViewById(R.id.accept);
        Button dismiss = dialogView.findViewById((R.id.dismiss));

        final AlertDialog b = dialogBuilder.create();
        b.show();

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseRequest.child(request.getKey()).child("request_type").setValue(Request_type.APPROVED);

                //update client's order history
                FirebaseDatabase.getInstance().getReference("ClientUser").child(request.getClientId()).child("history").push().setValue(request.getMealID());
                Toast.makeText(getActivity(), "Approved!", Toast.LENGTH_SHORT).show();
            }
        });

        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseRequest.child(request.getKey()).child("request_type").setValue(Request_type.REJECTED);
                Toast.makeText(getActivity(), "Rejected", Toast.LENGTH_SHORT).show();
            }
        });
    }
}