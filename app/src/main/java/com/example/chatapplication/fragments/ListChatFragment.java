package com.example.chatapplication.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.chatapplication.R;
import com.example.chatapplication.adapters.RecyclerViewAdapter;
import com.example.chatapplication.models.UserModel;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Circle;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class ListChatFragment extends Fragment {
    RecyclerView rcvListFriend;
    RecyclerViewAdapter adapter;
    ArrayList<UserModel> models;
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list_chat, container, false);
        models = new ArrayList<>();

        readUsers();

        initView(view);

        return view;
    }

    private void initView(View view) {
        rcvListFriend = view.findViewById(R.id.rcv_list_friends);
        rcvListFriend.hasFixedSize();
        rcvListFriend.setLayoutManager(new LinearLayoutManager(getContext()));
        progressBar = view.findViewById(R.id.spin_kit);
        Sprite circle = new Circle();
        progressBar.setIndeterminateDrawable(circle);
    }

    public void readUsers() {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        reference.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                models.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    UserModel user = new UserModel();
                    user.setUserId((Objects.requireNonNull(snapshot.child("user_id").getValue())).toString());
                    user.setUserName((Objects.requireNonNull(snapshot.child("user_name").getValue())).toString());

                    assert firebaseUser != null;
                    if (!user.getUserId().equals(firebaseUser.getUid())) {
                        models.add(user);
                    }
                }
                adapter = new RecyclerViewAdapter(models, getContext(), 0);
                rcvListFriend.setAdapter(adapter);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public ListChatFragment() {
    }
}
