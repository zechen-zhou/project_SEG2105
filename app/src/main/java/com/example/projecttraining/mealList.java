package com.example.projecttraining;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.projecttraining.databinding.FragmentMealListBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class mealList extends Fragment {

    private FragmentMealListBinding binding;
    private List<Meal> mealList;
    private ListView MealListView;
    private OfferedMealsList Adapter;

    Button add = binding.addButton;

    DatabaseReference databaseMeals = FirebaseDatabase.getInstance().getReferenceFromUrl("https://mealer-dd302-default-rtdb.firebaseio.com/");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMealListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @NonNull Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mealList = new ArrayList<>();
        MealListView = binding.mealsList;

        databaseMeals.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Meal meal = postSnapshot.getValue(Meal.class);
                    mealList.add(meal);
                }
                Adapter = new OfferedMealsList(getActivity(), mealList);
                MealListView.setAdapter(Adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        add.setOnClickListener(click -> {
            Navigation.findNavController(view).navigate(R.id.action_menu_add);
        });

        MealListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Meal meal = mealList.get(i);
                showDeleteMeal(meal.getId());
            }
        });


    }

    private void showDeleteMeal(String mealId) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.layout_delete_meal, null);
        dialogBuilder.setView(dialogView);

        Button delete = (Button) dialogView.findViewById(R.id.deleteMeal);

        final AlertDialog b = dialogBuilder.create();
        b.show();

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteProduct(mealId);
                b.dismiss();
            }
        });


    }





    private boolean deleteProduct(String id) {
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("Meals").child(id);
        dR.removeValue();
        Toast.makeText(getActivity(), "Meal deleted", Toast.LENGTH_LONG).show();
        return true;
    }





}
