package com.example.teachapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;


public class AccountFragment extends Fragment {

   private Button logoutB;

    public AccountFragment() {
        // Required empty public constructor
        Log.d("ddf","df");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account,container,false);

        logoutB = view.findViewById(R.id.logoutB);

        logoutB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                FirebaseAuth.getInstance().signOut();
            }
        });
        return view;
    }
}