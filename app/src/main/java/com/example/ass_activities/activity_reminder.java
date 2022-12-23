package com.example.ass_activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

public class activity_reminder extends AppCompatActivity {

    Gson gson = new Gson();

    private TextView remSubject;
    private TextView remDetails;
    private TextView viewRemDate;
    private Button remDelete;

    private boolean extend = false;

    private ImageButton addReminder;
    private AlertDialog dialog;
    private LinearLayout layout;

    private DatePickerDialog datePicker;
    private Button datePickerBtn;

    private EditText title;
    private EditText subject;
    private EditText details;

    private SharedPreferences Prefs;
    private android.content.SharedPreferences.Editor editor;
    public static final boolean FLAG = true;

    private ArrayList<Card> cards = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        addReminder = findViewById(R.id.addReminder);
        layout = findViewById(R.id.reminderContainer);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);



        setUpSharedPreferences();
//        remEditor.clear();
//        remEditor.commit();
        checkData();

        buildDialog();

        addReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });

    }

    private void checkData() {
        boolean flag = Prefs.getBoolean("REMINDER_FLAG", false);
        if (flag) {
            String strList = Prefs.getString("cards", null);
            Type listType = new TypeToken<ArrayList<Card>>() {
            }.getType();
            cards = gson.fromJson(strList, listType);

            for (Card card : cards) {
                addCard(card.getTitle(), card.getSubject(), card.getDetails(), card.getDate());
            }
        }
    }

    private void setUpSharedPreferences() {
        Prefs = PreferenceManager.getDefaultSharedPreferences(this);
        editor = Prefs.edit();
    }

    private void save() {

        String strList = gson.toJson(cards);
        editor.putBoolean("REMINDER_FLAG", FLAG);
        editor.putString("cards", strList);
        editor.apply();

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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
                        Card card = new Card(title.getText().toString(), details.getText().toString(), datePickerBtn.getText().toString());
                        card.setSubject(subject.getText().toString());
                        cards.add(card);
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

        datePicker = new DatePickerDialog(this, 0, dateSetListener, year, month, day);
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
                Card card = new Card(cardTitle.getText().toString(), cardDetails.getText().toString(), cardDate.getText().toString());
                card.setSubject(cardSubject.getText().toString());
                for (Card x : cards) {
                    if(x.getTitle() == card.getTitle() && x.getDate() == card.getDate() && x.getDetails() == card.getDetails() && x.getSubject() == card.getSubject()){
                        cards.remove(x);
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
                this.finish();
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


    @Override
    protected void onStop() {
        super.onStop();
        save();
    }

}