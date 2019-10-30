package com.example.chatapplication.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.chatapplication.R;
import com.example.chatapplication.models.UserModel;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Circle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private EditText edtUserName;
    private EditText edtEmail;
    private EditText edtPassword;
    private EditText edtConfirmPassword;
    private EditText edtPhoneNumber;
    private EditText edtAddress;
    private EditText edtBirthday;
    private Button btnRegister;
    private RadioButton rbMale, rbFemale;
    private ProgressBar progressBar;
    private RelativeLayout layout;
    FirebaseAuth auth;
    DatabaseReference reference;
    String TAG = "aa";
    UserModel user;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mapped();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String email = edtEmail.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();
                String passwordConfirm = edtConfirmPassword.getText().toString().trim();
                String phoneNumber = edtPhoneNumber.getText().toString().trim();
                String birthday = edtBirthday.getText().toString().trim();
                String address = edtAddress.getText().toString().trim();
                boolean isMale = rbMale.isChecked();
                user.setUserName(edtUserName.getText().toString());
                user.setAddress(edtAddress.getText().toString());
                user.setBirthday(edtBirthday.getText().toString());
                user.setPhoneNumber(edtPhoneNumber.getText().toString());
                user.setMale(isMale);
                if(!password.equals(passwordConfirm)){
                    Toast.makeText(RegisterActivity.this, "The password does not match", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }else {
                    register(email, password, user);
                }
            }
        });

        edtBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choiceDate();
            }
        });
        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard(v);
                return false;
            }
        });
    }

    private void mapped() {
        edtUserName = findViewById(R.id.edt_user_name);
        edtEmail = findViewById(R.id.edt_email_register);
        edtPassword = findViewById(R.id.edt_password_register);
        edtAddress = findViewById(R.id.edt_address);
        edtPhoneNumber = findViewById(R.id.edt_phone_number);
        edtBirthday = findViewById(R.id.edt_birthday);
        rbMale = findViewById(R.id.rb_male);
        rbFemale = findViewById(R.id.rb_female);
        edtConfirmPassword = findViewById(R.id.edt_confirm_password);
        btnRegister = findViewById(R.id.btn_register);

        user = new UserModel();
        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        progressBar = findViewById(R.id.spin_kit);
        Sprite circle = new Circle();
        progressBar.setIndeterminateDrawable(circle);
        layout = findViewById(R.id.rl_login);
    }

    public void register(String email, final String password, final UserModel user) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user1 = auth.getCurrentUser();

                            assert user1 != null;
                            user.setUserId(user1.getUid());

                            HashMap<String, String> map = new HashMap<>();
                            map.put("user_id", user.getUserId());
                            map.put("user_name", user.getUserName());
                            map.put("phone_number", user.getPhoneNumber());
                            map.put("gender", user.isMale() ? "male" : "female");
                            map.put("address", user.getAddress());
                            map.put("birthday", user.getBirthday());
                            map.put("imageURL", user.getUlrImage());

                            reference.child(user.getUserId()).setValue(map)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent);
                                                finish();
                                            }
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e(TAG, "onFailure: " + e.getMessage());
                                        }
                                    });
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: " + e.getMessage());
                    }
                });
    }

    public void hideKeyboard(View view) {
        InputMethodManager methodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (methodManager != null) {
            methodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void choiceDate(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
                edtBirthday.setText(format.format(calendar.getTime()));
            }
        }, 2017, 1,1);

        datePickerDialog.show();
    }
}
