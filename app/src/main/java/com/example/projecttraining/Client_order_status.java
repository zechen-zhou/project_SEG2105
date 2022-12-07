package com.example.projecttraining;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.projecttraining.databinding.FragmentClientOrderStatusBinding;
import com.example.projecttraining.user.Client;

public class Client_order_status extends Fragment {

    private FragmentClientOrderStatusBinding binding;
    private Request request;
    private Client user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentClientOrderStatusBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            request = (Request) bundle.getParcelable("request");
            user = (Client) bundle.getParcelable("clientUser");
        }

        String userName = user.getEmail();

        if (userName == request.getClientId()) {
            TextView status = binding.status;
            status.setText(request.getRequest_type().name());
        } else {
            Toast.makeText(getActivity(),"You don't have any order now", Toast.LENGTH_SHORT).show();
        }
    }

}
