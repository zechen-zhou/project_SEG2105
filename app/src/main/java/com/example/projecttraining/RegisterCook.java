package com.example.projecttraining;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projecttraining.databinding.FragmentRegisterCookBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.color.MaterialColors;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.example.projecttraining.user.Cook;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;

public class RegisterCook extends Fragment {

    Login login;
    private FragmentRegisterCookBinding binding;
    Bitmap thumbnail;

    Context context;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://mealer-dd302-default-rtdb.firebaseio.com/");
    FirebaseStorage storage = FirebaseStorage.getInstance();

    //return an Image from the Camera Activity
    ActivityResultLauncher<Intent> cameraResult = registerForActivityResult(        //it is safe to call registerForActivityResult() before your fragment or activity (camera activity) is created
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    ImageView profileImage = binding.voidChequeImageView;
                    Intent data = result.getData();                             //the camera activity sends back a Bitmap in the Intent data object
                    thumbnail = data.getParcelableExtra("data");   //Android stores the image under the variable name "data", and it's saved as a Bitmap object (the type of file format for image)
                    if (thumbnail != null) {
                        profileImage.setImageBitmap(thumbnail);                 //set the profileImage with the returned Bitmap object (thumbnail)
                    }
                }
            });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRegisterCookBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button welcomeCook = binding.submit;
        Button backToSignIn = binding.backToSignIn;
        ImageView voidChequeImageView = binding.voidChequeImageView;

        EditText firstname = binding.cookFirstNameInputEditText;
        EditText lastname = binding.cookLastNameInputEditText;
        EditText emailInputEditText = binding.emailInputEditText;
        TextInputLayout emailInputLayout = binding.emailInputLayout;
        EditText password = binding.cooknewPasswordEdittext;
        EditText address = binding.cookhomeAddressEdittext;
        EditText description = binding.cookdescriptionEdittext;

        TextView captureVoidChequeTextView = binding.captureVoidChequeTextView;

        emailInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //remove emailInputLayout error when the text length is changed
                emailInputLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        voidChequeImageView.setOnClickListener(click -> {
            //transition to built-in activities on the phone (take a picture)
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            //launch the cameraIntent
            cameraResult.launch(cameraIntent);
        });

        welcomeCook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cook cook;

                //get data from EditText from String variables
                String firstnameText = firstname.getText().toString();
                String lastnameText = lastname.getText().toString();
                String oddEmailText = emailInputEditText.getText().toString();
                String emailText;
                String passwordText = password.getText().toString();
                String addressText = address.getText().toString();
                String descriptionText = description.getText().toString();

                // Email is empty
                if (oddEmailText.equals("")) {
                    emailInputLayout.setError(getString(R.string.email_empty_message));

                    // Remove the error icon, so it will keep using the "clear_text" mode
                    emailInputLayout.setErrorIconDrawable(null);
                } else if (!login.isValidEmail(oddEmailText)) { // Email is not valid
                    emailInputLayout.setError(getString(R.string.email_error_message));

                    // Remove the error icon, so it will keep using the "clear_text" mode
                    emailInputLayout.setErrorIconDrawable(null);
                }

                // Void cheque is not uploaded
                if (thumbnail == null) {
                    context = getContext();
                    if (context != null) {
                        // Set error text and color
                        captureVoidChequeTextView.setText(R.string.capture_void_cheque_error_message);
                        captureVoidChequeTextView.setTextColor(ContextCompat.getColor(context, com.google.android.material.R.color.design_default_color_error));
                    }
                } else {
                    captureVoidChequeTextView.setText(R.string.capture_void_cheque_text);
                    captureVoidChequeTextView.setTextColor(ContextCompat.getColor(context, R.color.black));
                }

                // check if user fill all the fields
                if (firstnameText.isEmpty() || lastnameText.isEmpty() || !login.isValidEmail(oddEmailText) || passwordText.isEmpty() || addressText.isEmpty() || descriptionText.isEmpty() || thumbnail == null) {
                    Toast.makeText(getActivity(), "Please fill up all fields", Toast.LENGTH_SHORT).show();
                } else {
                    emailText = oddEmailText.replace('.', ',');
                    Context context = getActivity();
                    cook = new Cook(firstnameText, lastnameText, emailText, passwordText, addressText, descriptionText);


                    databaseReference.child("CookUser").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(emailText)) {
                                Toast.makeText(context, "Email is registered", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "Registration complete! Please login", Toast.LENGTH_SHORT).show();

                                databaseReference.child("CookUser").child(emailText).child("firstname").setValue(cook.getFirstName());
                                databaseReference.child("CookUser").child(emailText).child("lastname").setValue(cook.getLastName());
                                databaseReference.child("CookUser").child(emailText).child("password").setValue(cook.getPassword());
                                databaseReference.child("CookUser").child(emailText).child("address").setValue(cook.getAddress());
                                databaseReference.child("CookUser").child(emailText).child("description").setValue(cook.getCookDescription());
                                databaseReference.child("CookUser").child(emailText).child("status").setValue(0);
                                databaseReference.child("CookUser").child(emailText).child("rating").setValue(0);

                                if (thumbnail != null) {
                                    // Points to the root reference
                                    StorageReference storageRef = storage.getReference();

                                    // Points to "cook_images"
                                    StorageReference cookImagesRef = storageRef.child("cook_images");

                                    // Points to "cook_images/cookEmail_void_cheque.jpg"
                                    String fileName = emailText + "_void_cheque.jpg";
                                    StorageReference voidChequeRef = cookImagesRef.child(fileName);

                                    // Get the data from a Bitmap as bytes
                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                    thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                    byte[] data = baos.toByteArray();

                                    UploadTask uploadTask = voidChequeRef.putBytes(data);
                                    uploadTask.addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception exception) {
                                            // Handle unsuccessful uploads
                                            Log.d("RegisterCook", "Upload Failed");

                                        }
                                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                                            // ...
                                            Log.d("RegisterCook", "Upload Complete");
                                        }
                                    });
                                }

                                Navigation.findNavController(view).navigate(R.id.action_registerCook_to_login);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

        backToSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate((R.id.action_registerCook_to_login));
            }
        });
    }
}

