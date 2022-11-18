package com.example.projecttraining;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projecttraining.databinding.FragmentOfferedMealsBinding;
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

        offeredMealsListView = binding.mealsList;

        mealList = new ArrayList<>();
        fromSuspended = new ArrayList<>();

        SearchView searchbar = binding.mealSearch;

        searchbar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(mealList.contains(query)){
                    offeredAdapter.getFilter().filter(query);
                }
                else{
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

                for (DataSnapshot snap:snapshot.getChildren()) {
                    cookStatus.put(snap.getKey(), snap.child("status").getValue(Integer.class)==0);
                }

                //re-check list
                for (Meal m:mealList) {
                    if (!cookStatus.get(m.getCookUser())) { //previously active, now suspended
                        mealList.remove(m);
                        fromSuspended.add(m);
                    }
                }

                for (Meal m:fromSuspended) {
                    if (cookStatus.get(m.getCookUser())) { //previously suspended, now active
                        mealList.add(m);
                        fromSuspended.remove(m);
                    }
                }

                offeredAdapter = new OfferedMealsList(getActivity(), mealList);
                offeredMealsListView.setAdapter(offeredAdapter);
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

                for(DataSnapshot postSnapshot : snapshot.getChildren()){
                    Meal meal = postSnapshot.getValue(Meal.class);

                    boolean isOffered = postSnapshot.child("offered").getValue(Boolean.class);

                    if (isOffered && cookStatus.get(meal.getCookUser())) {
                        mealList.add(meal);

                    } else if (isOffered) {
                        fromSuspended.add(meal);
                    }

                }
                offeredAdapter = new OfferedMealsList(getActivity(), mealList);
                offeredMealsListView.setAdapter(offeredAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}