package com.example.ass_activities;

import static android.widget.Toast.*;

import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

public class TodoFormDialogFragment extends DialogFragment {
    private Button btnSubmit, btnCancel;
    private EditText etTitle, etDetails;
    private DatePicker datePicker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.todo_form, container, false);
//        btnSubmit = view.findViewById(R.id.btnSubmit);
//        btnCancel = view.findViewById(R.id.btnCancel);
        etTitle = view.findViewById(R.id.etTitle);
        etDetails = view.findViewById(R.id.etDetails);
        datePicker = view.findViewById(R.id.datePicker);



        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TodoFragment activity = new TodoFragment();
                FragmentManager fm = getActivity().getFragmentManager();
                String title = etTitle.getText().toString();
                String details = etDetails.getText().toString();

                if (validate(title, details)){
                    activity.addTodo(title, details, getDate());
                    dismiss();
                }else{
                    Toast.makeText(getActivity(), "Title and Details can not be empty", LENGTH_LONG).show();
                }


            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return view;
    }

    private String getDate(){

        String year = String.valueOf(datePicker.getYear());
        String month = String.valueOf(datePicker.getMonth() + 1);
        String day = String.valueOf(datePicker.getDayOfMonth());

        return "Due Date: " + year + " / " + month + " / " + day;
    }

    private boolean validate(String title, String details){
        if(title.equals("") || details.equals("")){
            return false;
        }
        return true;
    }


}
