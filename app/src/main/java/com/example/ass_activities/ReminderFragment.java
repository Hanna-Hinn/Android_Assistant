package com.example.ass_activities;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;


public class ReminderFragment extends Fragment {

    Gson gson = new Gson();

    private TextView remSubject;
    private TextView remDetails;
    private TextView viewRemDate;
    private Button remDelete;

    private boolean extend = false;

    private FloatingActionButton addReminder;
    private AlertDialog dialog;
    private LinearLayout layout;

    private DatePickerDialog datePicker;
    private Button datePickerBtn;

    private EditText title;
    private EditText subject;
    private EditText details;

    private SharedPreferences Prefs;
    private android.content.SharedPreferences.Editor editor;

    private ArrayList<Reminder> reminders = new ArrayList<>();

    private FirebaseDatabase database;
    private DatabaseReference ref;
    private String userID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_reminder, container, false);

        setUpDatabase();

        addReminder = rootView.findViewById(R.id.addReminder);
        layout = rootView.findViewById(R.id.reminderContainer);

        buildDialog();


        //Read the Data from the database
        new FirebaseDataBaseHelper().readReminder(new FirebaseDataBaseHelper.DataStatus() {
            @Override
            public void TodosIsLoaded(List<Todo> data, List<String> keys) {

            }

            @Override
            public void ReminderIsLoaded(List<Reminder> data, List<String> keys) {
                reminders.clear();
                for (Reminder x:data) {
                    reminders.add(x);
                    addCard(x.getTitle(),x.getSubject(),x.getDetails(),x.getDate());
                }

            }

            @Override
            public void GradeIsLoaded(HashMap<String,SubjectGrade> data) {

            }

            @Override
            public void SubjectGradesIsLoaded(List<Mark> data, List<String> keys) {

            }
        },userID);

        addReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });

        return rootView;
    }

    private void setUpDatabase(){
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("reminders");
        SharedPreferences Prefs = getActivity().getSharedPreferences("Auth", MODE_PRIVATE);
        userID = Prefs.getString("user",null);
    }


    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        month = month + 1;

        return day + "-" + month + "-" + year;
    }

    private void buildDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getLayoutInflater().inflate(R.layout.dialog_reminder, null);


        title = view.findViewById(R.id.remTitle);
        subject = view.findViewById(R.id.remSub);
        details = view.findViewById(R.id.remDetail);
        datePickerBtn = view.findViewById(R.id.datePickerBtn);

        datePickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePicker(view);
            }
        });


        title.setText("");
        subject.setText("");
        details.setText("");
        datePickerBtn.setText(getTodaysDate());

        initDatePicker();


        builder.setView(view);
        builder.setTitle("Add Reminder")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Reminder reminder = new Reminder(title.getText().toString(),subject.getText().toString(), details.getText().toString(), datePickerBtn.getText().toString());
                        addToDatabase(reminder);
                        reminders.add(reminder);
                        addCard(title.getText().toString(), subject.getText().toString(), details.getText().toString(), datePickerBtn.getText().toString());
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        dialog = builder.create();

    }

    //Write into the database
    private void addToDatabase(Reminder obj){
        ref.child(userID).child(""+obj.getId()).child("title").setValue(obj.getTitle());
        ref.child(userID).child(""+obj.getId()).child("subject").setValue(obj.getSubject());
        ref.child(userID).child(""+obj.getId()).child("details").setValue(obj.getDetails());
        ref.child(userID).child(""+obj.getId()).child("date").setValue(obj.getDate());
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = day + "-" + month + "-" + year;
                datePickerBtn.setText(date);
            }
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        datePicker = new DatePickerDialog(getActivity(), 0, dateSetListener, year, month, day);
    }

    private void openDatePicker(View view) {
        datePicker.show();
    }


    private void addCard(String title, String subject, String details, String dueDate) {
        final View view = getLayoutInflater().inflate(R.layout.card_reminder, null);

        TextView cardTitle = view.findViewById(R.id.cardRemTitle);
        TextView cardSubject = view.findViewById(R.id.cardRemSubject);
        TextView cardDetails = view.findViewById(R.id.cardRemDetails);
        TextView cardDate = view.findViewById(R.id.cardRemDate);
        Button delete = view.findViewById(R.id.remDelete);

        cardTitle.setText(title);
        cardSubject.setText(subject);
        cardDetails.setText(details);
        cardDate.setText(dueDate);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Reminder reminder = new Reminder(cardTitle.getText().toString(),cardSubject.getText().toString(), cardDetails.getText().toString(), cardDate.getText().toString());
                for (Reminder x : reminders) {
                    if(x.getTitle() == reminder.getTitle() && x.getDate() == reminder.getDate() && x.getDetails() == reminder.getDetails() && x.getSubject() == reminder.getSubject()){
//                        Query reminderQuery = ref.child(""+x.getId());
//                        reminderQuery.addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                snapshot.getRef().removeValue();
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//
//                            }
//                        });
                        reminders.remove(x);
                        break;
                    }
                }
                layout.removeView(view);
            }
        });


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                extend = !extend;
                showRemDetails(extend, view);
            }
        });


        layout.addView(view);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showRemDetails(boolean extend, View view) {
        remSubject = view.findViewById(R.id.cardRemSubject);
        remDetails = view.findViewById(R.id.cardRemDetails);
        viewRemDate = view.findViewById(R.id.cardRemDate);
        remDelete = view.findViewById(R.id.remDelete);

        if (extend) {
            remSubject.setVisibility(View.VISIBLE);
            remDetails.setVisibility(View.VISIBLE);
            remDelete.setVisibility(View.VISIBLE);
            viewRemDate.setTextSize(30);
        } else {
            remSubject.setVisibility(View.GONE);
            remDetails.setVisibility(View.GONE);
            remDelete.setVisibility(View.GONE);
            viewRemDate.setTextSize(15);
        }
    }

}