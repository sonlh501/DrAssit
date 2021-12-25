package com.example.drassit.ui.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.drassit.DriverAssitant;
import com.example.drassit.MainActivity;
import com.example.drassit.R;
import com.example.drassit.databinding.FragmentHomeBinding;
import com.example.drassit.ui.gas.FillGas;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    ImageButton btnFillGas;
    ImageButton btnRepair;
    ImageButton btnSos;
    FirebaseAuth mAuth;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        final TextView textView = binding.textHome;
//        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        btnFillGas = binding.btnFillGas;
        btnRepair = binding.btnRepair;
        btnSos = binding.btnSos;

        btnFillGas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fillGas();
            }
        });


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void fillGas(){
        Intent intent = new Intent(getActivity(), FillGas.class);
        startActivity(intent);
    }
}