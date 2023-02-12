package com.example.ass_activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class activity_subject_grades extends AppCompatActivity {
    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private static TextView txtSubjectName;
    private static TextView txtSubjectTotal;

    private static FirebaseDatabase database;
    private static DatabaseReference ref;
    private static String userID;

    private static String subject;
    private static String total;
    private static Float realFullGrade = Float.valueOf(0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_grades);

        Intent intent = getIntent();
        subject = intent.getStringExtra("SUBJECT_NAME");
        total = intent.getStringExtra("TOTAL");

        setUpDatabase();

        setUpViews();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SubjectGradeFormDialogFragment subjectGradeFormDialogFragment = new SubjectGradeFormDialogFragment();
                subjectGradeFormDialogFragment.show(getSupportFragmentManager(), "form");
            }
        });

        ArrayList<String> titles = new ArrayList<>();
        ArrayList<String> grades = new ArrayList<>();
        ArrayList<String> realGrades = new ArrayList<>();

        // Here is the code which suppose to get the saved grades from the database
        // the code has to fill the grades in the above ArrayLists
        new FirebaseDataBaseHelper().readSubjectGrades(new FirebaseDataBaseHelper.DataStatus() {

            @Override
            public void SubjectGradesIsLoaded(List<Mark> data, List<String> keys) {
                titles.clear();
                grades.clear();
                realGrades.clear();
                for(Mark x: data){
                    titles.add(x.getTitle());
                    grades.add(x.getGrade());
                    realGrades.add(x.getReal_grade());
                    realFullGrade += Float.parseFloat(x.getFull_grade());
                }

                String str = "Total: " +total+ " of ( "+ realFullGrade +" / 100)";
                txtSubjectTotal.setText(str);

                recyclerView.setLayoutManager(new LinearLayoutManager(activity_subject_grades.this));

                GradeAdapter adapter = new GradeAdapter(titles, grades, realGrades);
                recyclerView.setAdapter(adapter);


            }
            @Override
            public void TodosIsLoaded(List<Todo> data, List<String> keys) {

            }

            @Override
            public void ReminderIsLoaded(List<Reminder> data, List<String> keys) {

            }

            @Override
            public void GradeIsLoaded(HashMap<String, SubjectGrade> data) {

            }
        },userID,subject);


    }

    private void setUpViews() {
        fab = findViewById(R.id.GradesFab);
        recyclerView = findViewById(R.id.gradesRv);
        txtSubjectName = findViewById(R.id.txtSubjectName);
        txtSubjectTotal = findViewById(R.id.txtSubjectTotal);
    }

    private void setUpDatabase(){
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("grades");
        SharedPreferences Prefs = getSharedPreferences("Auth", MODE_PRIVATE);
        userID = Prefs.getString("user",null);
    }


    public void addGrade(String title, String grade, String fullGrade, String realGrade, String realFullGrade) {
        GradeAdapter adapter = (GradeAdapter) recyclerView.getAdapter();

        String finalGrade = grade + " / " + fullGrade;
        String finalRealGrade = realGrade + " / " + realFullGrade;

        adapter.addItem(title, finalGrade, finalRealGrade);
        String total = txtSubjectTotal.getText().toString();

        ref.child(userID).child(subject).child("marks").child(""+adapter.getItemCount()).child("title").setValue(title);
        ref.child(userID).child(subject).child("marks").child(""+adapter.getItemCount()).child("grade").setValue(grade);
        ref.child(userID).child(subject).child("marks").child(""+adapter.getItemCount()).child("full_grade").setValue(fullGrade);
        ref.child(userID).child(subject).child("marks").child(""+adapter.getItemCount()).child("real_grade").setValue(realGrade);
        ref.child(userID).child(subject).child("marks").child(""+adapter.getItemCount()).child("real_full").setValue(realFullGrade);

        updateTotal(realGrade, realFullGrade, true);
    }

    static void updateTotal(String realGrade, String realFullGrade, boolean isAdd) {
        int numRealGrade = Integer.parseInt(realGrade);
        int numRealFullGrade = Integer.parseInt(realFullGrade);

        String total = txtSubjectTotal.getText().toString();

        int totalGrade = Integer.parseInt(total.split(":")[1].split(" ")[1]);
        int totalFullGrade = Integer.parseInt(total.split(":")[1].split(" ")[3].split("\\(")[1]);

        int newTotalGrade;
        int newTotalFullGrade;

        if (isAdd) {
            newTotalGrade = totalGrade + numRealGrade;
            newTotalFullGrade = totalFullGrade + numRealFullGrade;
        } else {
            newTotalGrade = totalGrade - numRealGrade;
            newTotalFullGrade = totalFullGrade - numRealFullGrade;
        }



        String strNewTotalGrade = String.valueOf(newTotalGrade);
        String strNewTotalFullGrade = String.valueOf(newTotalFullGrade);

        String newTotal = "Total: " + strNewTotalGrade + " of (" + strNewTotalFullGrade + " / 100)";

        ref.child(userID).child(subject).child("total").setValue(strNewTotalGrade);

        txtSubjectTotal.setText(newTotal);
    }

    @Override
    public boolean onSupportNavigateUp() {
        // Handle the back button click
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if(fragmentManager.getBackStackEntryCount() > 0){
            fragmentManager.popBackStack();
        }else{
            super.onBackPressed();
        }
    }

    public static void deleteFromDatabase(int position){
        Log.d("subjectName", txtSubjectName.getText().toString());
        String subjectName = txtSubjectName.getText().toString().split(":")[1].split(" ")[1];
        ref = database.getReference("grades/" + userID + "/" + subjectName + "/marks");


        // Add a listener to retrieve the list of child nodes
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                int i = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (i == position) {
                        // This is the node you want to delete
                        Log.d("test", snapshot.getKey().toString());
                        snapshot.getRef().removeValue();
                        break;
                    }
                    i++;
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



