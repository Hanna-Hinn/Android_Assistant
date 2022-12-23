package com.example.ass_activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
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

public class activity_view_grade extends AppCompatActivity {
    // linked list for saving every gradeView we make
    ArrayList<Grade> gradesViews = new ArrayList<>();

    private final boolean FLAG = true;

    Gson gson = new Gson();

    private TotalGrade totalGRade = new TotalGrade(0, 0);

    private Button btnAddGrade;
    private EditText editGradeTitle;
    private EditText editAchievedGrade;
    private EditText editFullGrade;
    private EditText editFullRealGrade;
    private TextView txtSubjectGrade;

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    private String subject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_grade);
        subject = getIntent().getStringExtra("SUBJECT_NAME");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(subject);

        initialiseInputs();
        setUpSharedPreferences();
//        viewGradeEditor.clear();
//        viewGradeEditor.commit();
        checkData();


        btnAddGrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // getting input from the input fields
                String gradeTitle = editGradeTitle.getText().toString();
                String grade = editAchievedGrade.getText().toString();
                String fullGrade = editFullGrade.getText().toString();
                String fullRealGrade = editFullRealGrade.getText().toString();

                // input Validation
                if (validateInput()) {

                    // building the gradeView
                    buildGrade(gradeTitle, grade, fullGrade, fullRealGrade, true);

                    // after building and showing the gradeView, make the input fields empty
                    editGradeTitle.setText("");
                    editAchievedGrade.setText("");
                    editFullGrade.setText("");
                    editFullRealGrade.setText("");


                } else {
                    Toast.makeText(activity_view_grade.this, "Fields can not be empty", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void setUpSharedPreferences() {
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        editor = prefs.edit();
    }

    private void checkData() {
        boolean flag = prefs.getBoolean(subject+"FLAG", false);
        if (flag) {
            String strList = prefs.getString(subject + "grades", null);
            Type listType = new TypeToken<ArrayList<Grade>>() {
            }.getType();
            gradesViews = gson.fromJson(strList, listType);

            for (Grade grade : gradesViews) {
                String strGrade = grade.getGrade().split(" ")[0];
                String strFullGrade = grade.getGrade().split(" ")[2];
                String strRealFullGrade = grade.getRealGrade().split(" ")[2];
                buildGrade(grade.getTitle(), strGrade, strFullGrade, strRealFullGrade, false);
            }
        }
    }

    private void initialiseInputs() {
        txtSubjectGrade = findViewById(R.id.txtSubjectGrade);
        btnAddGrade = findViewById(R.id.btnAddGrade);
        editAchievedGrade = findViewById(R.id.editAchievedGrade);
        editGradeTitle = findViewById(R.id.editGradeTitle);
        editFullGrade = findViewById(R.id.editFullGrade);
        editFullRealGrade = findViewById(R.id.editFullRealGrade);
    }

    private void buildGrade(String title, String strGrade, String strFullGrade, String strRealFullGrade, boolean isFirstTime) {
        LinearLayout layout = findViewById(R.id.gradesViewsLayout);
        LayoutInflater inflater = LayoutInflater.from(this);
        View gradeView = inflater.inflate(R.layout.grade_layout, layout, false);

        // declaring elements of the grade view
        TextView txtGradeTitle = gradeView.findViewWithTag("tgGradeTitle");
        TextView txtGrade = gradeView.findViewWithTag("tgGrade");
        TextView txtRealGrade = gradeView.findViewWithTag("tgRealGrade");
        Button btnDeleteGrade = gradeView.findViewWithTag("tgBtnDeleteGrade");


        // parsing String inputs into floats
        float grade = Float.parseFloat(strGrade);
        float fullGrade = Float.parseFloat(strFullGrade);
        float realFullGrade = Float.parseFloat(strRealFullGrade);

        // Calculate the realGrade
        int realGrade = calculateRealGrade(grade, fullGrade, realFullGrade);


        // modify the total grade
        totalGRade.addGrade(realGrade, (int) realFullGrade);
        txtSubjectGrade.setText(totalGRade.toString());

        // getting the output ready
        String outGrade = "Grade: " + (int) grade + " / " + (int) fullGrade;
        String outRealGrade = "Real Grade: " + realGrade + " / " + (int) realFullGrade;

        // Add the gradeView to the main layout
        layout.addView(gradeView);
        Grade grade1 = new Grade(title, strGrade + " / " + strFullGrade, realGrade + " / " + strRealFullGrade);

        if (isFirstTime) {
            gradesViews.add(grade1);
            save();
        }


        btnDeleteGrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // simply set the visibility for the gradeView to gone
                gradeView.setVisibility(View.GONE);
                // when deleting a grade, this method will modify the total grade
                totalGRade.deleteGrade(realGrade, (int) realFullGrade);
                txtSubjectGrade.setText(totalGRade.toString());

                for (int i = 0; i < gradesViews.size(); i++) {
                    if (gradesViews.get(i).getTitle().equals(title) && gradesViews.get(i).getGrade().equals(grade1.getGrade())
                            && gradesViews.get(i).getRealGrade().equals(grade1.getRealGrade())) {
                        gradesViews.remove(i);
                        break;
                    }
                }
                save();
            }
        });

        // inserting the right values in the grade view fields
        txtGradeTitle.setText(title);
        txtGrade.setText(outGrade);
        txtRealGrade.setText(outRealGrade);

    }

    private int calculateRealGrade(float grade, float fullGrade, float fullRealGrade) {

        float ratio = fullGrade / fullRealGrade;

        return (int) (grade / ratio);

    }


    private boolean validateInput() {

        if (editGradeTitle.getText().toString().equals("") || editAchievedGrade.getText().toString().equals("")
                || editFullGrade.getText().toString().equals("") || editFullRealGrade.getText().toString().equals("")) {
            return false;
        }
        return true;
    }

    private void save() {

        String strList = gson.toJson(gradesViews);
        editor.putBoolean(subject+"FLAG", FLAG);
        editor.putString(subject + "grades", strList);
        editor.apply();


    }

    @Override
    protected void onStop() {
        super.onStop();
        Intent intent = new Intent(activity_view_grade.this,activity_grades.class);
        intent.putExtra("grade",totalGRade.getAchievedGrade());
        setResult(RESULT_OK, intent);
        finish();
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