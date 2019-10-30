package com.example.chatapplication.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.example.chatapplication.R;
import com.example.chatapplication.adapters.ChatAdapter;
import com.example.chatapplication.models.ChatModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class MessageActivity extends AppCompatActivity {
    private EditText edtContent;
    private TextView tvUserName;
    private ImageButton btnSend, btnBack;
    String TAG = "aa";
    private RecyclerView rcv_message;
    private ChatAdapter adapter;
    private List<ChatModel> chat;
    DatabaseReference reference;
    private String receiverId;
    private String senderId;
    private String imageURL;
    private RelativeLayout layout;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        mapped();

        readMessage("default");


        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!edtContent.equals("")) {
                    String message = edtContent.getText().toString();
                    sendMessage(senderId, receiverId, message);
                    edtContent.setText("");
                }
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard(v);
                return false;
            }
        });
        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                   if(receiverId.equals(snapshot.child("user_id").getValue().toString())){
                       imageURL = snapshot.child("imageURL").getValue().toString();
                   }
                   readMessage(imageURL);
               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void hideKeyboard(View view) {
        InputMethodManager methodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (methodManager != null) {
            methodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void readMessage(String imageUrl) {
        chat = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("message_content");
        reference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chat.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String sender = snapshot.child("sender").getValue().toString();
                    String receiver = snapshot.child("receiver").getValue().toString();
                    String message = snapshot.child("message").getValue().toString();

                    if (sender.equals(senderId) && receiver.equals(receiverId) || sender.equals(receiverId) && receiver.equals(senderId)) {
                        chat.add(new ChatModel(sender, receiver, message));
                    }
                }
                adapter = new ChatAdapter(chat, MessageActivity.this, imageURL);
                adapter.notifyDataSetChanged();
                rcv_message.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void mapped() {
        senderId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        receiverId = getIntent().getStringExtra("userId");
        rcv_message = findViewById(R.id.rcv_message);
        rcv_message.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        rcv_message.setLayoutManager(manager);
        edtContent = findViewById(R.id.edt_message);
        btnSend = findViewById(R.id.imb_send_text);
        tvUserName = findViewById(R.id.tv_user_name);
        tvUserName.setText(getIntent().getStringExtra("userName"));
        btnBack = findViewById(R.id.btn_back);
        layout = findViewById(R.id.rl_login);
    }

    public void sendMessage(String sender, String receiver, String message) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> map = new HashMap<>();
        map.put("sender", sender);
        map.put("receiver", receiver);
        map.put("message", message);

        reference.child("message_content").push().setValue(map);
    }
}
