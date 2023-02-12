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
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GradeFragment extends Fragment {

    Gson gson = new Gson();

    private Button addGrade;
    private AlertDialog dialog;
    private LinearLayout layout;

    private HashMap<String, SubjectGrade> grades = new HashMap<>();

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

        buildDialog();


        new FirebaseDataBaseHelper().readGrades(new FirebaseDataBaseHelper.DataStatus() {
            @Override
            public void GradeIsLoaded(HashMap<String,SubjectGrade> data) {
                grades.clear();
                for(String i: data.keySet()){
                    grades.put(i,data.get(i));
                    addCard(i, Float.parseFloat(data.get(i).getTotal()));
                }
            }

            @Override
            public void SubjectGradesIsLoaded(List<Mark> data, List<String> keys) {

            }

            @Override
            public void TodosIsLoaded(List<Todo> data, List<String> keys) {
            }

            @Override
            public void ReminderIsLoaded(List<Reminder> data, List<String> keys) {
            }
        },userID);

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
        ref = database.getReference("grades");
        SharedPreferences Prefs = getActivity().getSharedPreferences("Auth", MODE_PRIVATE);
        userID = Prefs.getString("user",null);
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
                        String subject = name.getText().toString();
                        SubjectGrade marks = new SubjectGrade();
                        grades.put(subject,marks);
                        addCard(subject,0);

                        ref.child(userID).child(subject).setValue(new SubjectGrade());

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

                for (String x : grades.keySet()) {
                    if (x == subject.getText().toString()) {
                        deleteFromDatabase(x);
                        grades.remove(x);
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
                intent.putExtra("TOTAL", grades.get(subject.getText().toString()).getTotal());
                startActivity(intent);
            }
        });


        layout.addView(view);
    }

    private void deleteFromDatabase(String name){
        ref = database.getReference("grades/" + userID);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    if (name.equals(snapshot.getKey().toString())) {
                        // This is the node you want to delete
                        snapshot.getRef().removeValue();
                        break;
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle the error
            }
        });

        ref = database.getReference("grades");
    }




}