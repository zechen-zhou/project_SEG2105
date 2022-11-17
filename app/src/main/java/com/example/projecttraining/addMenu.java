package com.example.projecttraining;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.projecttraining.databinding.FragmentAddMenuBinding;
import com.example.projecttraining.user.Cook;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class addMenu extends Fragment {

    private FragmentAddMenuBinding binding;

    private Cook currentUser;

    DatabaseReference db = FirebaseDatabase.getInstance().getReference("Meals");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceSate) {
        binding = FragmentAddMenuBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return  root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            currentUser = (Cook) bundle.getParcelable("cookUser");
        }

        Button submit = binding.add;

        EditText meal_name = binding.mealName;
        EditText meal_type = binding.mealType;
        EditText meal_ingredients = binding.mealIngredients;
        EditText meal_allergens = binding.mealAllergens;
        EditText meal_price = binding.mealPrice;
        EditText meal_description = binding.mealDescription;
        Button back = binding.addMenuBackBtn;

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Meal meal;

                String meal_nameText = meal_name.getText().toString();
                String meal_typeText = meal_type.getText().toString();
                String meal_ingredientsText = meal_ingredients.getText().toString();
                String meal_allergensText = meal_allergens.getText().toString();
                String meal_priceNumber = meal_price.getText().toString();
                String meal_descriptionText = meal_description.getText().toString();

                if (meal_nameText.isEmpty() || meal_typeText.isEmpty() || meal_ingredientsText.isEmpty() || meal_allergensText.isEmpty() || meal_descriptionText.isEmpty()) {
                    Toast.makeText(getActivity(), "Please fill up all fields", Toast.LENGTH_SHORT).show();

                } else {
                    try {
                        Context context = getActivity();
                        double price = Double.parseDouble(meal_priceNumber);
                        ArrayList<String> ingredientsList = new ArrayList<String>(Arrays.asList(meal_ingredientsText.split(",")));
                        ArrayList<String> allergensList = new ArrayList<String>(Arrays.asList(meal_allergensText.split(",")));
                        String id = db.push().getKey();

                        meal = new Meal(id, meal_nameText, meal_typeText, ingredientsList, allergensList, price, meal_descriptionText, currentUser.getEmail());

                        db.child("Meal").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.hasChild(meal_nameText)) {
                                    Toast.makeText(context, "The meal is added", Toast.LENGTH_SHORT).show();
                                } else {
                                    db.child(id).setValue(meal);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        Toast.makeText(getActivity(), "Meal Added!", Toast.LENGTH_SHORT).show();
                        Navigation.findNavController(view).navigate(R.id.action_addMenu_to_menu, bundle);

                    } catch (IllegalArgumentException e) {
                        Toast.makeText(getActivity(), "price must be a number", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        back.setOnClickListener(click -> {
            Navigation.findNavController(view).navigate(R.id.action_addMenu_to_menu, bundle);
        });
    }

}
