package com.example.projecttraining;

import java.util.ArrayList;

public class Meal {

    private String _mealName;
    private String _mealType;
    private ArrayList<String> _ingredients;
    private ArrayList<String> _allergens;
    private double _price;
    private String _description;
    private String _cookUser;
    private boolean _offered;

    public Meal() {}

    public Meal(String name, String type, ArrayList ingredients, ArrayList allergens, double price, String description, String cook) {
        this._mealName = name;
        this._mealType = type;
        this._ingredients = ingredients;
        this._allergens = allergens;
        this._price = price;
        this._description = description;
        this._cookUser = cook;
        this._offered = false;
    }


    //getters and setters
    public String getMealName() {
        return _mealName;
    }

    public void setMealName(String _mealName) {
        this._mealName = _mealName;
    }

    public String getMealType() {
        return _mealType;
    }

    public void setMealType(String _mealType) {
        this._mealType = _mealType;
    }

    public ArrayList<String> getIngredients() {
        return _ingredients;
    }

    public void setIngredients(ArrayList<String> _ingredients) {
        this._ingredients = _ingredients;
    }

    public ArrayList<String> getAllergens() {
        return _allergens;
    }

    public void setAllergens(ArrayList<String> _allergens) {
        this._allergens = _allergens;
    }

    public double getPrice() {
        return _price;
    }

    public void setPrice(double _price) {
        this._price = _price;
    }

    public String getDescription() {
        return _description;
    }

    public void setDescription(String _description) {
        this._description = _description;
    }

    public String getCookUser() {
        return _cookUser;
    }

    public void setCookUser(String _cookUser) {
        this._cookUser = _cookUser;
    }

    public boolean isOffered() {
        return _offered;
    }

    public void setOffered(boolean offered) {
        this._offered = offered;
    }

}
