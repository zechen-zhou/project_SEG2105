package com.example.projecttraining;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.projecttraining.databinding.FragmentOfferedMealsBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OfferedMeals extends Fragment {

    private FragmentOfferedMealsBinding binding;
    private ListView offeredMealsListView;
    private List<Meal> mealList;

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

        databaseMeals = FirebaseDatabase.getInstance().getReference("Meals");
        databaseCooks = FirebaseDatabase.getInstance().getReference("CookUser");
        offeredMealsListView = binding.mealsList;

        mealList = new ArrayList<>();


        //DELETE LATER
        //Used for testing only -- click "Offered Meals" Title to add meal into the database
        TextView title = binding.offeredMealsTitle;

        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = databaseMeals.push().getKey();

                ArrayList<String> ingredients = new ArrayList<>();
                ingredients.add("A");
                ingredients.add("B");
                ArrayList<String> allergens = new ArrayList<>();
                allergens.add("z");
                Meal meal = new Meal("name", "type", ingredients, allergens,12.32, "description", "888" );
                meal.setOffered(true);
                databaseMeals.child(id).setValue(meal);
            }
        });
        //end of testing only


        //get all offered meals
        databaseMeals.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mealList.clear();

                for(DataSnapshot postSnapshot : snapshot.getChildren()){
                    Meal meal = postSnapshot.getValue(Meal.class);

                    boolean isOffered = postSnapshot.child("offered").getValue(Boolean.class);

                    //TODO: figure out how to check cook's status without the blank list error
//                    databaseCooks.child(meal.getCookUser()).child("status").addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snap) {
//                            boolean cookActive = snap.getValue(Integer.class)==0;
//                            if (isOffered && cookActive) {
//                                mealList.add(meal);
//                            }
//                            return;
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//                        }
//                    });

                    if (isOffered) {
                        mealList.add(meal);
                    }

                }
                OfferedMealsList offeredAdapter = new OfferedMealsList(getActivity(), mealList);
                offeredMealsListView.setAdapter(offeredAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}