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
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class activity_subject_grades extends AppCompatActivity {
    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private TextView txtSubjectName;
    private static TextView txtSubjectTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_grades);

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

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        GradeAdapter adapter = new GradeAdapter(titles, grades, realGrades);
        recyclerView.setAdapter(adapter);

    }

    private void setUpViews() {
        fab = findViewById(R.id.GradesFab);
        recyclerView = findViewById(R.id.gradesRv);
        txtSubjectName = findViewById(R.id.txtSubjectName);
        txtSubjectTotal = findViewById(R.id.txtSubjectTotal);

        // when we implement the database,` we have to delete these 2 lines of code
        String total = "Total: 0 of (0 / 100)";
        txtSubjectTotal.setText(total);
    }

    public void addGrade(String title, String grade, String fullGrade, String realGrade, String realFullGrade) {
        GradeAdapter adapter = (GradeAdapter) recyclerView.getAdapter();

        String finalGrade = grade + " / " + fullGrade;
        String finalRealGrade = realGrade + " / " + realFullGrade;

        adapter.addItem(title, finalGrade, finalRealGrade);
        String total = txtSubjectTotal.getText().toString();
        updateTotal(realGrade, realFullGrade, true);
    }

    public static void updateTotal(String realGrade, String realFullGrade, boolean isAdd) {
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
}



