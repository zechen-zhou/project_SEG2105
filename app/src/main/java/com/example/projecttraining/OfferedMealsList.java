package com.example.projecttraining;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OfferedMealsList extends ArrayAdapter<Meal> implements Filterable {

    private Activity context;
    private List<Meal> meals;
    List<Meal> mStringFilterList; //added this line

    public OfferedMealsList (Activity context, List<Meal> meals) {
        super(context, R.layout.layout_offeredmeals_list, meals);
        this.context = context;
        this.meals = meals;
        mStringFilterList = meals; //added this line
    }

    public int getCount() {
        return meals.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View listView = inflater.inflate(R.layout.layout_offeredmeals_list, null, true);

        TextView textMealName = listView.findViewById(R.id.mealNameText);
        TextView price = listView.findViewById(R.id.priceText);
        TextView textMealType = listView.findViewById(R.id.mealTypeText);
        TextView textCuisine = listView.findViewById(R.id.cuisineTypeText);
        TextView byCook = listView.findViewById(R.id.byCookText);
        TextView ingredients = listView.findViewById(R.id.ingredientsText);
        TextView allergens = listView.findViewById(R.id.allergensText);
        TextView textDescription = listView.findViewById(R.id.mealDescriptionText);

        if (!(position<meals.size())){
            return listView;
        }

        Meal meal = meals.get(position);
        textMealName.setText(meal.getMealName());
        DecimalFormat priceFormat = new DecimalFormat("0.00");
        price.setText("$"+String.valueOf(priceFormat.format(meal.getPrice())));

        textMealType.setText("Type: "+meal.getMealType());
        textCuisine.setText("Cuisine: "+meal.getCuisineType());
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
        textDescription.setText("Description: " + meal.getDescription());

        return listView;
    }

    //implement the filter logic (filter by name) -- youtube videos and google how to create filter
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults results = new FilterResults();
                if(charSequence != null && charSequence.length() > 0){
                    ArrayList<Meal> filteredMeals = new ArrayList<Meal>();
                    for(int i = 0 ; i < mStringFilterList.size() ; i++){
                        if(mStringFilterList.get(i).getMealName().toUpperCase().contains(charSequence.toString().toUpperCase())){
                            //Meal meal = new Meal(mStringFilterList.get(i).getMealName(),
                            //        mStringFilterList.get(i).getMealType()); //create a new meal that takes as parameters the attributes
                            filteredMeals.add(mStringFilterList.get(i));
                        }
                    }
                    results.count = filteredMeals.size();
                    results.values = filteredMeals;
                } else{
                    results.count = mStringFilterList.size();
                    results.values = mStringFilterList;
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults results) {
                meals = (ArrayList<Meal>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
