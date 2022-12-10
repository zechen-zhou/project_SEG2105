package com.example.projecttraining;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class RequestList extends ArrayAdapter<Request> {

    private Activity context;
    private List<Request> requests;
    DatabaseReference databaseMeal;

    public RequestList (Activity context, List<Request> requests) {
        super (context, R.layout.layout_cook_request_list, requests);
        this.context = context;
        this.requests = requests;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View listView = inflater.inflate(R.layout.layout_cook_request_list,null,true);

        Request request = requests.get(position);

        TextView textMealName = listView.findViewById(R.id.forMealnameText);
        databaseMeal = FirebaseDatabase.getInstance().getReference("Meals").child(request.getMealID());

        databaseMeal.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Meal meal = snapshot.getValue(Meal.class);
                textMealName.setText("Meal: "+meal.getMealName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        textMealName.setText("For: "+request.getMealID());

        TextView textClient = listView.findViewById(R.id.fromClientTextView);
        textClient.setText("From: "+request.getClientId());

        TextView status = listView.findViewById(R.id.requestStatus);
        status.setText(request.getRequest_type().toString());

        return listView;
    }
}
