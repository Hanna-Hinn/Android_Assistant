package com.example.ass_activities;


import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class GradeFragment extends Fragment {

    Gson gson = new Gson();

    private Button addGrade;
    private AlertDialog dialog;
    private LinearLayout layout;

    private ArrayList<ViewGrade> viewGrades = new ArrayList<>();

    private String intentGrade;

    private FirebaseDatabase database;
    private DatabaseReference ref;
    private String userID;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_grade, container, false);

        intentGrade = getActivity().getIntent().getStringExtra("grade");

        addGrade = rootView.findViewById(R.id.addGrade);
        layout = rootView.findViewById(R.id.gradeContainer);

        setUpDatabase();

        //Read the Data from the database
        ref.child(userID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    Log.d(TAG, "Result: " + task.getResult().getValue().toString());
                }
            }
        });

        buildDialog();

        addGrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });


        return rootView;
    }

    private void setUpDatabase(){
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("todos");
        SharedPreferences Prefs = getActivity().getSharedPreferences("Auth", MODE_PRIVATE);
        userID = Prefs.getString("user",null);
    }

    private void addGradetoDatabase(){

    }


    private void buildDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getLayoutInflater().inflate(R.layout.dialog_grade, null);

        final EditText name = view.findViewById(R.id.addSubName);

        builder.setView(view);
        builder.setTitle("Enter name")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ViewGrade viewGrade = new ViewGrade(name.getText().toString());
                        viewGrades.add(viewGrade);
                        addCard(name.getText().toString(), 0);
                        ref.child(userID).child(name.getText().toString());
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        dialog = builder.create();
    }

    private void addCard(String name, float viewGrade) {
        final View view = getLayoutInflater().inflate(R.layout.card_grade, null);

        TextView subject = view.findViewById(R.id.subjectName);
        TextView grade = view.findViewById(R.id.subjectGrade);
        Button delete = view.findViewById(R.id.gradeDelete);

        subject.setText(name);
        grade.setText(String.valueOf(viewGrade));
        if(intentGrade != null){
            grade.setText(intentGrade);
        }

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (ViewGrade x : viewGrades) {
                    if (x.getSubject() == subject.getText().toString()) {
                        viewGrades.remove(x);
                        break;
                    }
                }
                layout.removeView(view);
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), activity_subject_grades.class);
                intent.putExtra("SUBJECT_NAME", subject.getText().toString());
                startActivity(intent);
            }
        });


        layout.addView(view);
    }






}