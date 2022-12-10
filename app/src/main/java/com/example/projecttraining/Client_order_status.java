package com.example.projecttraining;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.projecttraining.databinding.FragmentClientOrderStatusBinding;
import com.example.projecttraining.user.Client;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Client_order_status extends Fragment {

    private FragmentClientOrderStatusBinding binding;
    private List<Request> orders;
    private ListView orderListView;
    private RequestList Adapter;
    private Client user;

    DatabaseReference dbRequest = FirebaseDatabase.getInstance().getReference("Request");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentClientOrderStatusBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            user = (Client) bundle.getParcelable("clientUser");
        }

        orderListView = binding.clientStatusList;
        orders = new ArrayList<>();

        dbRequest.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    Request request = postSnapshot.getValue(Request.class);

                    //if request if from this user
                    if (request.getClientId().equals(user.getEmail())) {
                        orders.add(request);
                    }
                }

                Activity activity = getActivity();
                if (activity != null) {
                    Adapter = new RequestList(getActivity(), orders);
                    orderListView.setAdapter(Adapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}
