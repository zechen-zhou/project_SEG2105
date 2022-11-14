package com.example.projecttraining;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.DecimalFormat;
import java.util.List;

public class OfferedMealsList extends ArrayAdapter<Meal> {

    private Activity context;
    private List<Meal> meals;

    public OfferedMealsList (Activity context, List<Meal> meals) {
        super(context, R.layout.layout_offeredmeals_list, meals);
        this.context = context;
        this.meals = meals;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View listView = inflater.inflate(R.layout.layout_offeredmeals_list, null, true);

        TextView textMealName = listView.findViewById(R.id.mealNameText);
        TextView price = listView.findViewById(R.id.priceText);
        TextView textMealType = listView.findViewById(R.id.mealTypeText);
        TextView byCook = listView.findViewById(R.id.byCookText);
        TextView ingredients = listView.findViewById(R.id.ingredientsText);
        TextView allergens = listView.findViewById(R.id.allergensText);
        TextView textDescription = listView.findViewById(R.id.mealDescriptionText);

        Meal meal = meals.get(position);
        textMealName.setText(meal.getMealName());
        DecimalFormat priceFormat = new DecimalFormat("0.00");
        price.setText("$"+String.valueOf(priceFormat.format(meal.getPrice())));

        textMealType.setText("Type: "+meal.getMealType());
        byCook.setText("By: "+meal.getCookUser().replace(",","."));

        StringBuilder ingrStr = new StringBuilder("");
        for (String i:meal.getIngredients()) {
            ingrStr.append(i).append(",");
        }
        String ingredientsList = ingrStr.toString();
        ingredients.setText("Ingredients: " + ingredientsList.substring(0, ingredientsList.length()-1));

        StringBuilder allerStr = new StringBuilder("");
        for (String a:meal.getAllergens()) {
            allerStr.append(a).append(",");
        }
        String allergensList = allerStr.toString();
        allergens.setText("Allergens: " + allergensList.toString().substring(0, allergensList.length()-1));
        textDescription.setText("Description: "+meal.getDescription());

        return listView;
    }
}
