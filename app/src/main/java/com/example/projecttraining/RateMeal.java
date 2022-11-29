package com.example.projecttraining;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projecttraining.databinding.FragmentRateMealBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RateMeal extends Fragment {

    private FragmentRateMealBinding binding;
    private String mealName;
    private String cookID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRateMealBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //TODO: get meal and cook ID/email from previous fragment -- placeholder value
        cookID = "kiv@mail,com";
        mealName = "Spaghetti";

        //display meal and cook name
        TextView meal = binding.mealNametext;
        TextView cook = binding.bycookText;

        meal.setText(mealName);
        cook.setText("By: "+cookID);

        //rating logic
        DatabaseReference databaseCook = FirebaseDatabase.getInstance().getReference("CookUser").child(cookID);

        RatingBar ratingBar = (RatingBar) binding.mealRatingBar;
        Button submitBtn = binding.mealRatingSubmitBtn;

        submitBtn.setOnClickListener(view1 -> {
            float rating = ratingBar.getRating();

            if (rating==0) {
                Toast.makeText(getActivity(), "please rate first", Toast.LENGTH_SHORT).show();
                return;
            }

            //update cook rating
            databaseCook.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    float curRating = snapshot.child("rating").getValue(Float.class);

                    //add to cook's average rating
                    if (curRating==0) {//has no current ratings
                        databaseCook.child("rating").setValue(rating);
                    } else {
                        databaseCook.child("rating").setValue((curRating + rating)/2);
                    }

                    Toast.makeText(getActivity(), "thanks for rating!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });

        });

    }
}