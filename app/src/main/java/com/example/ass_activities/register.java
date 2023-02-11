package com.example.ass_activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

public class register extends AppCompatActivity {

    Gson gson = new Gson();

    private EditText register_username;
    private EditText register_password;
    private EditText confirm_password;
    private TextView login_user;
    private Button register;

    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    private ProgressDialog progressDialog;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private SharedPreferences Prefs;
    private android.content.SharedPreferences.Editor editor;

    private int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        setUp();

        login_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchActivities();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performRegister();
            }
        });

    }


    private void setUp() {
        register_username = findViewById(R.id.register_username);
        register_password = findViewById(R.id.register_password);
        confirm_password = findViewById(R.id.confirm_password);
        login_user = findViewById(R.id.login_user);
        register = findViewById(R.id.register);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
    }

    private void switchActivities() {
        Intent switchActivityIntent = new Intent(this, login.class);
        startActivity(switchActivityIntent);
    }

    private void performRegister() {
        String email = register_username.getText().toString();
        String pass = register_password.getText().toString();
        String confirm = register_password.getText().toString();

        if (validateInputs(email, pass, confirm)) {
            progressDialog.setMessage("Please Wait until the Process is finished");
            progressDialog.setTitle("Registration");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressDialog.dismiss();
                    if (task.isSuccessful()) {
                        Toast.makeText(com.example.ass_activities.register.this, "User Made Successfully", Toast.LENGTH_SHORT).show();
                        mUser = mAuth.getCurrentUser();
                        addUsertoDB();
                        setUpPreference();
                        sendUsertoActivity();
                    } else {
                        Toast.makeText(com.example.ass_activities.register.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }


    }

    private void addUsertoDB() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference();
        ref.child("users").child(mUser.getUid()).child("email").setValue(mUser.getEmail());
    }

    private void sendUsertoActivity() {
        Intent intent = new Intent(com.example.ass_activities.register.this, MainActivity2.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void setUpPreference() {
        Prefs = getSharedPreferences("Auth", MODE_PRIVATE);
        editor = Prefs.edit();
        editor.putString("user", mUser.getUid().toString());
        editor.commit();
    }


    private boolean validateInputs(String email, String pass, String confirm) {
        if (!email.matches(emailPattern) || email.isEmpty()) {
            register_username.setError("InValid Email");
            register_username.setBackgroundResource(R.drawable.error);
//            Toast.makeText(this, "InValid Email, Please Try Again", Toast.LENGTH_SHORT).show();
            return false;
        } else if (pass.isEmpty() || pass.length() < 6) {
            register_password.setError("InValid Password");
            register_password.setBackgroundResource(R.drawable.error);
//            Toast.makeText(this, "InValid Password, Please Try Again", Toast.LENGTH_SHORT).show();
            return false;
        } else if (confirm.isEmpty() || confirm.compareTo(pass) != 0) {
            confirm_password.setError("Password Does not match");
            confirm_password.setBackgroundResource(R.drawable.error);
//            Toast.makeText(this, "InValid Password, Please Try Again", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            register_username.setBackgroundResource(R.drawable.normal);
            register_password.setBackgroundResource(R.drawable.normal);
            confirm_password.setBackgroundResource(R.drawable.normal);
            return true;
        }
    }
}

