package com.example.ass_activities;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.app.ActionBar;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class activity_grades extends AppCompatActivity {

    Gson gson = new Gson();

    private Button addGrade;
    private AlertDialog dialog;
    private LinearLayout layout;

    private SharedPreferences prefs;
    private android.content.SharedPreferences.Editor editor;
    public static final boolean FLAG = true;

    private ArrayList<ViewGrade> viewGrades = new ArrayList<>();

    private String intentGrade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grades);
        intentGrade = getIntent().getStringExtra("grade");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        addGrade = findViewById(R.id.addGrade);
        layout = findViewById(R.id.gradeContainer);

        setUpSharedPreferences();
//        gradeEditor.clear();
//        gradeEditor.commit();
        checkData();

        buildDialog();

        addGrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });

    }

    private void setUpSharedPreferences() {
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        editor = prefs.edit();
    }

    private void save() {


        String strList = gson.toJson(viewGrades);
        editor.putBoolean("GRADE_FLAG", FLAG);
        editor.putString("grades", strList);
        editor.apply();


    }

    private void checkData() {
        boolean flag = prefs.getBoolean("GRADE_FLAG", false);
        if (flag) {
            String strList = prefs.getString("grades", null);
            Type listType = new TypeToken<ArrayList<ViewGrade>>() {
            }.getType();
            viewGrades = gson.fromJson(strList, listType);

            for (ViewGrade grade : viewGrades) {
                addCard(grade.getSubject(), grade.getGrade());
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        save();
    }

    private void buildDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
                Intent intent = new Intent(activity_grades.this, activity_view_grade.class);
                intent.putExtra("SUBJECT_NAME", subject.getText().toString());
                startActivity(intent);
                finish();
            }
        });


        layout.addView(view);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK ) {
            if (data.hasExtra("grade")) {
                final View view = getLayoutInflater().inflate(R.layout.card_grade, null);
                TextView grade = view.findViewById(R.id.subjectGrade);
                grade.setText(data.getExtras().getString("grade"));
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}