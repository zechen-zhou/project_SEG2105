package com.example.projecttraining;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.projecttraining.databinding.FragmentWelcomeCookBinding;
import com.example.projecttraining.user.Cook;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WelcomeCook extends Fragment {

    private FragmentWelcomeCookBinding binding;
    private Cook currentUser;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://mealer-dd302-default-rtdb.firebaseio.com/");

    private static final int COOK_ACCOUNT_NORMAL = 0;
    private static final int COOK_ACCOUNT_TEMPORARILY_SUSPENDED = 1;
    private static final int COOK_ACCOUNT_PERMANENTLY_SUSPENDED = 2;

    String theEmail;
    String suspensionDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentWelcomeCookBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            currentUser = (Cook) bundle.getParcelable("cookUser");
        }

        TextView firstName = binding.firstName;
        TextView lastName = binding.lastName;
        TextView email = binding.emailAddress;
        TextView address = binding.homeAddress;
        TextView description = binding.shortDescription;

        Button logout = binding.logout;
        Button accessMenu = binding.accessMenu;
        Button myPurchaseRequest = binding.purchaseRequestsButton;

        //display corresponding Cook information on screen
        firstName.setText(currentUser.getFirstName());
        lastName.setText(currentUser.getLastName());
        String convertEmail = currentUser.getEmail();
        theEmail = convertEmail.replace('.', ',');
        email.setText(theEmail);
        address.setText(currentUser.getAddress());
        description.setText(currentUser.getCookDescription());

        logout.setOnClickListener(click -> {
            Navigation.findNavController(view).navigate(R.id.action_welcomeCook_to_login);
        });

        accessMenu.setOnClickListener(click -> {
            Navigation.findNavController(view).navigate(R.id.action_welcomeCook_to_menu, bundle);
        });

        myPurchaseRequest.setOnClickListener(click -> {
            Navigation.findNavController(view).navigate(R.id.action_welcomeCook_to_purchaseRequest, bundle);
        });

        // Check the cook's account status and show the account warning dialog if the account has been temporarily or permanently suspended
        databaseReference.child("CookUser").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                if (snapshot.hasChild(theEmail)) {
                    int cookAccountStatus = snapshot.child(theEmail).child("status").getValue(Integer.class);

                    if (cookAccountStatus != COOK_ACCOUNT_NORMAL) {

                        // Retrieve the suspension date
                        databaseReference.child("Complaints").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                    if (theEmail.equals(postSnapshot.child("cookUser").getValue(String.class))) {
                                        suspensionDate = postSnapshot.child("suspensionDate").getValue(String.class);

                                        // Put the following code inside this block that we use to retrieve the data from firebase, otherwise it will create the dialog before receiving the data from database

                                        // Load an alert window that shows a message about the temporary suspension of the account
                                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                                        // Set a custom layout for dialog
                                        LayoutInflater inflater = getLayoutInflater();
                                        View dialogView = inflater.inflate(R.layout.cook_account_warning_dialog, null);
                                        builder.setView(dialogView);

                                        TextView dialogTitleText = dialogView.findViewById(R.id.dialogTitleText);
                                        TextView accountWarningMessageText = dialogView.findViewById(R.id.accountWarningMessageText);
                                        Button okButton = dialogView.findViewById(R.id.okButton);

                                        // Set dialog title text
                                        dialogTitleText.setText(R.string.dialog_title);

                                        // Set account warning message
                                        switch (cookAccountStatus) {
                                            case COOK_ACCOUNT_TEMPORARILY_SUSPENDED:
                                                accountWarningMessageText.setText(getString(R.string.dialog_dear_text)
                                                        + " " + currentUser.getFirstName() + " "
                                                        + currentUser.getLastName() + ",\n\n"
                                                        + getString(R.string.account_temporarily_suspended_text)
                                                        + " " + suspensionDate + ". "
                                                        + getString(R.string.account_suspended_text));
                                                break;
                                            case COOK_ACCOUNT_PERMANENTLY_SUSPENDED:
                                                accountWarningMessageText.setText(getString(R.string.dialog_dear_text)
                                                        + " " + currentUser.getFirstName() + " "
                                                        + currentUser.getLastName() + ",\n\n"
                                                        + getString(R.string.account_permanently_suspended_text)
                                                        + " " + getString(R.string.account_suspended_text));
                                                break;
                                        }

                                        okButton.setOnClickListener(cl -> {
                                            // Finish the activity
                                            getActivity().finish();
                                        });

                                        // Prevent back button from closing the dialog box
                                        builder.setCancelable(false);

                                        // Make the alert window appear
                                        builder.create().show();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError error) {
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }
}