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

public class login extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private TextView register_user;
    private Button login;

    private SharedPreferences Prefs;
    private android.content.SharedPreferences.Editor editor;

    private ProgressDialog progressDialog;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setUpViews();
        setUpsharedPreferences();
        checkPreferences();

        register_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchActivities();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preformLogin();
            }


        });

    }

    private void setUpsharedPreferences() {
        Prefs = getSharedPreferences("Auth", MODE_PRIVATE);
        editor = Prefs.edit();
    }

    private void checkPreferences() {
        String userID = Prefs.getString("user", null);

        if (userID != null) {
            Intent intent = new Intent(this, MainActivity2.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

    }

    private void preformLogin() {
        String email = username.getText().toString();
        String pass = password.getText().toString();

        if (validateInputs(email, pass)) {
            progressDialog.setMessage("Please Wait until the Process is finished");
            progressDialog.setTitle("Logging In");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressDialog.dismiss();
                    if (task.isSuccessful()) {
                        Toast.makeText(com.example.ass_activities.login.this, "LoggedIn Successfully", Toast.LENGTH_SHORT).show();
                        mUser = mAuth.getCurrentUser();
                        setUpPreference();
                        sendUsertoActivity();
                    } else {
                        Toast.makeText(com.example.ass_activities.login.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

    }

    private void setUpPreference() {
        Prefs = getSharedPreferences("Auth", MODE_PRIVATE);
        editor = Prefs.edit();
        editor.putString("user", mUser.getUid().toString());
        editor.commit();
    }

    private boolean validateInputs(String email, String pass) {
        if (email.isEmpty()) {
            username.setError("Enter Email");
            username.setBackgroundResource(R.drawable.error);
            return false;
        } else if (pass.isEmpty()) {
            password.setError("Enter Email");
            password.setBackgroundResource(R.drawable.error);
            return false;
        } else {
            username.setBackgroundResource(R.drawable.normal);
            password.setBackgroundResource(R.drawable.normal);
            return true;
        }
    }

    private void setUpViews() {
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        register_user = findViewById(R.id.register_user);
        login = findViewById(R.id.login);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
    }

    private void sendUsertoActivity() {
        Intent intent = new Intent(com.example.ass_activities.login.this, MainActivity2.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    private void switchActivities() {
        Intent switchActivityIntent = new Intent(this, register.class);
        startActivity(switchActivityIntent);
    }
}