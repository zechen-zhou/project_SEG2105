package com.example.projecttraining;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.projecttraining.databinding.FragmentClientOrderHistoryBinding;
import com.example.projecttraining.user.Client;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Client_OrderHistory extends Fragment {

    private Client currentUser;

    private FragmentClientOrderHistoryBinding binding;
    private ListView historyListView;
    private OfferedMealsList listAdapter;
    private List<Meal> mealList;

    private DatabaseReference databaseHistory;
    private final DatabaseReference databaseMeals = FirebaseDatabase.getInstance().getReference("Meals");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentClientOrderHistoryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            currentUser = (Client) bundle.getParcelable("clientUser");
        }

        //initializations
        mealList = new ArrayList<>();
        historyListView = binding.orderHistoryListView;
        historyListView.setEmptyView(binding.emptyHistoryText);
        databaseHistory = FirebaseDatabase.getInstance().getReference("ClientUser").child(currentUser.getEmail()).child("history");

        //update listview to show client's order history
        databaseHistory.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mealList.clear();

                //get all mealIDs from order history
                for (DataSnapshot postSnapshot:snapshot.getChildren()) {

                    String mealID = postSnapshot.getValue(String.class);

                    //add meal information from ID
                    databaseMeals.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                                //if meal ids match, add to list and stop searching
                                if (postSnapshot.getKey().equals(mealID)) {
                                    Meal m = postSnapshot.getValue(Meal.class);
                                    mealList.add(m);

                                    //update list view
                                    listAdapter = new OfferedMealsList(getActivity(), mealList);
                                    historyListView.setAdapter(listAdapter);

                                    return;
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        //when users click on a meal --> open dialog letting them rate or submit a complaint
        historyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Meal meal = mealList.get(i);

                // create dialog with custom layout
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());

                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.client_order_history_dialog, null);
                dialogBuilder.setView(dialogView);

                // set displays to match selected meal
                TextView mealName = dialogView.findViewById(R.id.mealNametext2);
                TextView cook = dialogView.findViewById(R.id.bycookText2);

                mealName.setText(meal.getMealName());
                cook.setText("By: "+meal.getCookUser().replace(",", "."));

                //show dialog
                Dialog dialog = dialogBuilder.create();
                dialog.show();

                // set button listeners --> navigate to respective fragments
                Button rate = dialogView.findViewById(R.id.rateBtn);
                Button complain = dialogView.findViewById(R.id.complainBtn);

                rate.setOnClickListener(view1 ->{
                    dialog.dismiss();
                    Navigation.findNavController(view).navigate(R.id.action_client_OrderHistory_to_rateMeal);
                });

                complain.setOnClickListener(view1 ->{

                });

            }
        });

    }

}