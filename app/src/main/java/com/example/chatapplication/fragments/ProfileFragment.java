package com.example.chatapplication.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.chatapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class ProfileFragment extends DialogFragment {
    private TextView tvUserName, tvGender, tvPhoneNumber, tvAddress, tvBirthday;
    private DatabaseReference reference;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        initView(view);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Intent intent = getActivity().getIntent();
        final String uid = intent.getStringExtra("userId");
        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String userId = snapshot.child("user_id").getValue().toString();
                    Log.e(TAG, "onDataChange: "+userId );
                    if(userId.equals(user.getUid()) || userId.equals(uid)){
                        tvAddress.append(snapshot.child("address").getValue().toString());
                        tvBirthday.append(snapshot.child("birthday").getValue().toString());
                        tvGender.append(snapshot.child("gender").getValue().toString());
                        tvPhoneNumber.append(snapshot.child("phone_number").getValue().toString());
                        tvUserName.append(snapshot.child("user_name").getValue().toString());
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return view;
    }

    public ProfileFragment() {
    }

    public ProfileFragment(String tvUserName, String tvGender, String tvPhoneNumber, String tvAddress, String tvBirthday) {
        this.tvUserName.setText(tvUserName);
        this.tvGender.setText(tvGender);
        this.tvPhoneNumber.setText(tvPhoneNumber);
        this.tvAddress.setText(tvAddress);
        this.tvBirthday.setText(tvBirthday);
    }

    private void initView(View view) {
        tvAddress = view.findViewById(R.id.tv_addresss);
        tvBirthday = view.findViewById(R.id.tv_birthday);
        tvGender = view.findViewById(R.id.tv_gender);
        tvUserName = view.findViewById(R.id.tv_user_name);
        tvPhoneNumber = view.findViewById(R.id.tv_phone_number);
    }


}
