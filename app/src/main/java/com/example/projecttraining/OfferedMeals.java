package com.example.projecttraining;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.projecttraining.databinding.FragmentOfferedMealsBinding;
import com.example.projecttraining.user.Client;
import com.example.projecttraining.user.Cook;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OfferedMeals extends Fragment {

    private FragmentOfferedMealsBinding binding;
    private ListView offeredMealsListView;
    private List<Meal> mealList;
    private List<Meal> fromSuspended;
    private OfferedMealsList offeredAdapter;

    private Map<String, Boolean> cookStatus;

    DatabaseReference databaseMeals;
    DatabaseReference databaseCooks;
    DatabaseReference databaseOrder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentOfferedMealsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cookStatus = new HashMap<>();
        databaseMeals = FirebaseDatabase.getInstance().getReference("Meals");
        databaseCooks = FirebaseDatabase.getInstance().getReference("CookUser");

        Client currentUser = null;
        Meal meal = null;
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            currentUser = (Client) bundle.getParcelable("clientUser");
            meal = (Meal) bundle.getParcelable("meal");
        }

        String cookName = meal.getCookUser();
        databaseOrder = FirebaseDatabase.getInstance().getReference("Request/" + cookName);



        offeredMealsListView = binding.mealsList;

        mealList = new ArrayList<>();
        fromSuspended = new ArrayList<>();

        SearchView searchbar = binding.mealSearch;

        searchbar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (mealList.contains(query)) {
                    offeredAdapter.getFilter().filter(query);
                } else {
                    Toast.makeText(getActivity(), "Not found", Toast.LENGTH_LONG).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                offeredAdapter.getFilter().filter(newText);
                return false;
            }
        });


        //listens for cooks status changes and adds them to a map
        databaseCooks.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot snap : snapshot.getChildren()) {
                    Integer temp = snap.child("status").getValue(Integer.class);
                    if (temp != null) {
                        cookStatus.put(snap.getKey(), temp == 0);
                    }
                }

                //re-check list
                for (Meal m : mealList) {
                    Boolean temp = cookStatus.get(m.getCookUser());
                    if (temp != null) {
                        if (!temp) { //previously active, now suspended
                            mealList.remove(m);
                            fromSuspended.add(m);
                        }
                    }
                }

                for (Meal m : fromSuspended) {
                    Boolean temp = cookStatus.get(m.getCookUser());
                    if (temp != null) {
                        if (temp) { //previously suspended, now active
                            mealList.add(m);
                            fromSuspended.remove(m);
                        }
                    }
                }

                Activity activity = getActivity();

                if (activity != null) {
                    offeredAdapter = new OfferedMealsList(getActivity(), mealList);
                    offeredMealsListView.setAdapter(offeredAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //get all offered meals
        databaseMeals.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mealList.clear();

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Meal meal = postSnapshot.getValue(Meal.class);

                    Boolean isOffered = postSnapshot.child("offered").getValue(Boolean.class);

                    if (isOffered != null && meal != null) {
                        Boolean temp = cookStatus.get(meal.getCookUser());
                        if (temp != null) {
                            if (isOffered && temp) {
                                mealList.add(meal);

                            } else if (isOffered) {
                                fromSuspended.add(meal);
                            }
                        }
                    }

                }

                Activity activity = getActivity();

                if (activity != null) {
                    offeredAdapter = new OfferedMealsList(getActivity(), mealList);
                    offeredMealsListView.setAdapter(offeredAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Client finalCurrentUser = currentUser;
        offeredMealsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Meal meal = mealList.get(i);

                if (finalCurrentUser != null) {
                    String clientUser = finalCurrentUser.getEmail();
                    requestOrder(meal, clientUser);
                }

            }
        });
    }

    private void requestOrder(Meal meal, String clientUser) {
        Activity activity = getActivity();
        if (activity != null) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
            LayoutInflater inflater = getLayoutInflater();
            final View dialogView = inflater.inflate(R.layout.layout_request_order, null);
            dialogBuilder.setView(dialogView);

            Button order = dialogView.findViewById(R.id.order);
            Bundle bundle = this.getArguments();

            final AlertDialog b = dialogBuilder.create();
            b.show();

            order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String requestId = databaseOrder.push().getKey();
                    databaseOrder.push().setValue(new Request(requestId, clientUser, meal, Request_type.PENDING));

                    Toast.makeText(getActivity(), "thanks for ordering!", Toast.LENGTH_SHORT).show();
                    b.dismiss();
                }
            });
        }
    }
}