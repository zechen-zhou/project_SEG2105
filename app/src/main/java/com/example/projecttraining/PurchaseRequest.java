package com.example.projecttraining;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

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
    private List<Meal> requestList;
    private OfferedMealsList Adapter;
    private Request request;
    private Cook cookUser;

    DatabaseReference databaseRequest;

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
            request = (Request) bundle.getParcelable("request");
            cookUser = (Cook) bundle.getParcelable("cookUser");
        }

        String cookName = cookUser.getEmail();
        String requestId = request.getKey();
        databaseRequest = FirebaseDatabase.getInstance().getReference("request/" +cookName + "/" +requestId);

        databaseRequest.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                requestList.clear();

                for(DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Meal request = postSnapshot.getValue(Meal.class);
                    requestList.add(request);
                }
                Adapter = new OfferedMealsList(getActivity(), requestList);
                requestListView.setAdapter(Adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        requestListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Meal meal = requestList.get(i);
                String id = request.getKey();
                requestMeal(meal.getId(),id);

            }
        });
    }

    private void requestMeal(String mealId, String id) {
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
                databaseRequest.child(id).child("Request_type").setValue(Request_type.APPROVED);
            }
        });

        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseRequest.child(id).child("Request_type").setValue(Request_type.REJECTED);
            }
        });
    }
}