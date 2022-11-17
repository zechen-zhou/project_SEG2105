package com.example.projecttraining;

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
import androidx.navigation.Navigation;

import com.example.projecttraining.databinding.FragmentMealListBinding;
import com.example.projecttraining.user.Cook;
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

    private Cook currentUser;
    private Button add;
    private Button back;

    DatabaseReference databaseMeals = FirebaseDatabase.getInstance().getReference("Meals");

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

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            currentUser = (Cook) bundle.getParcelable("cookUser");
        }

        mealList = new ArrayList<>();
        MealListView = binding.mealsList;
        add = binding.addButton;
        back = binding.menuBackBtn;

        databaseMeals.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (getActivity()==null) {
                    return;
                }
                mealList.clear();

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Meal meal = postSnapshot.getValue(Meal.class);

                    if (meal.getCookUser().equals(currentUser.getEmail())) {
                        mealList.add(meal);
                    }
                }
                Adapter = new OfferedMealsList(getActivity(), mealList);
                MealListView.setAdapter(Adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        add.setOnClickListener(click -> {
            Navigation.findNavController(view).navigate(R.id.action_menu_add, bundle);
        });

        MealListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Meal meal = mealList.get(i);
                showUpdateMeal(meal.getId());
            }
        });

        back.setOnClickListener(click -> {
            Navigation.findNavController(view).navigate(R.id.action_menu_to_welcomeCook, bundle);
        });
    }

    private void showUpdateMeal(String mealId) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.layout_update_meal, null);
        dialogBuilder.setView(dialogView);

        Button delete = (Button) dialogView.findViewById(R.id.deleteMeal);
        Button offer = dialogView.findViewById(R.id.offerMealBtn);
        Button unoffer = dialogView.findViewById(R.id.unofferMealBtn);

        final AlertDialog b = dialogBuilder.create();
        b.show();

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                databaseMeals.child(mealId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        boolean offered = snapshot.child("offered").getValue(Boolean.class);

                        if (offered) {
                            Toast.makeText(getActivity(), "Can't delete offered meal", Toast.LENGTH_LONG).show();

                        } else {
                            deleteProduct(mealId);
                            b.dismiss();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });


        offer.setOnClickListener(view -> {
            databaseMeals.child(mealId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    boolean offered = snapshot.child("offered").getValue(Boolean.class);

                    if (offered) {
                        Toast.makeText(getActivity(), "Is already offered", Toast.LENGTH_LONG).show();

                    } else {
                        databaseMeals.child(mealId).child("offered").setValue(true);
                        Toast.makeText(getActivity(), "Meal offered!", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        });

        unoffer.setOnClickListener(view -> {
            databaseMeals.child(mealId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    boolean offered = snapshot.child("offered").getValue(Boolean.class);

                    if (!offered) {
                        Toast.makeText(getActivity(), "Already not offered", Toast.LENGTH_LONG).show();

                    } else {
                        databaseMeals.child(mealId).child("offered").setValue(false);
                        Toast.makeText(getActivity(), "Meal is unoffered", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        });
    }


    private boolean deleteProduct(String id) {
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("Meals").child(id);
        dR.removeValue();
        Toast.makeText(getActivity(), "Meal deleted", Toast.LENGTH_LONG).show();
        return true;
    }

}
