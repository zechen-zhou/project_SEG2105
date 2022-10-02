package com.example.projecttraining;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projecttraining.databinding.FragmentLoginBinding;

/**
 * Shows a login page
 */
public class Login extends Fragment {

    String[] items = {"Cook", "Client"};
    ArrayAdapter<String> adapterItems;

    private FragmentLoginBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView register = binding.register;
        Button login = binding.signIn;
        AutoCompleteTextView autoCompleteTxt = binding.autoCompleteTxt;

        autoCompleteTxt.setOnItemClickListener((AdapterView<?> parent, View view1, int position, long id) -> {
                String item = parent.getItemAtPosition(position).toString();
                Toast.makeText(getActivity(), "Item: " + item, Toast.LENGTH_SHORT).show();
        });

        register.setOnClickListener(click -> {
            // TODO: Update the navigate parameter (It's currently navigating to register cook as a demo)
            Navigation.findNavController(view).navigate(R.id.action_login_to_registerCook);
        });

        login.setOnClickListener(click -> {
            // TODO: Update the navigate parameter (It's currently navigating to welcome cook as a demo)
            Navigation.findNavController(view).navigate(R.id.action_login_to_welcomeCook);
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        AutoCompleteTextView autoCompleteTxt = binding.autoCompleteTxt;

        // Set adapter in onResume() so that the dropdownMenu can show all items even if we navigate
        // to other fragments and coming back
        adapterItems = new ArrayAdapter<String>(getActivity(), R.layout.list_item, items);
        autoCompleteTxt.setAdapter(adapterItems);
    }
}