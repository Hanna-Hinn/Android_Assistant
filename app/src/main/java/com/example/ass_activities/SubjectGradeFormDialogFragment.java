package com.example.ass_activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

public class SubjectGradeFormDialogFragment extends DialogFragment {

    private EditText etGradeTitle, etGrade, etFullGrade, etRealFullGrade;
    private Button btnGradeSubmit, btnGradeCancel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.subject_grade_form, container, false);

        setUpViews(view);

        btnGradeCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnGradeSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity_subject_grades activity = (activity_subject_grades) getActivity();
                if (validate()){


                    float grade = Float.parseFloat(etGrade.getText().toString());
                    float fullGrade = Float.parseFloat(etFullGrade.getText().toString());
                    float realFullGrade = Float.parseFloat(etRealFullGrade.getText().toString());

                    int realGrade = calculateRealGrade(grade, fullGrade, realFullGrade);
                    String title = etGradeTitle.getText().toString();

                    String strGrade = String.valueOf((int)grade);
                    String strFullGrade = String.valueOf((int)fullGrade);
                    String strRealGrade = String.valueOf(realGrade);
                    String strRealFullGrade = String.valueOf((int)realFullGrade);

                    activity.addGrade(title, strGrade, strFullGrade, strRealGrade, strRealFullGrade);

                    dismiss();
                }else{
                    Toast.makeText(activity, "Fields can not be empty", Toast.LENGTH_LONG).show();
                }

            }
        });

        return view;
    }

    private void setUpViews(View view) {
        etGradeTitle = view.findViewById(R.id.etGradeTitle);
        etGrade = view.findViewById(R.id.etGrade);
        etFullGrade = view.findViewById(R.id.etFullGrade);
        etRealFullGrade = view.findViewById(R.id.etRealFullGrade);
        btnGradeSubmit = view.findViewById(R.id.btnGradeSubmit);
        btnGradeCancel = view.findViewById(R.id.btnGradeCancel);
    }

    private int calculateRealGrade(float grade, float fullGrade, float realFullGrade){
        float ratio = fullGrade / realFullGrade;

        return (int) (grade / ratio);
    }

    private boolean validate(){
        if(etGradeTitle.getText().toString().equals("") || etGrade.getText().toString().equals("") || etFullGrade.getText().toString().equals("") || etRealFullGrade.getText().toString().equals("")){
            return false;
        }
        return true;
    }

}
