package com.example.ass_activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private CardView gradeCard;
    private CardView reminderCard;
    private CardView todoCard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gradeCard = findViewById(R.id.gradeCard);
        reminderCard = findViewById(R.id.reminderCard);
        todoCard = findViewById(R.id.todoCard);

        gradeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchActivities(1);
            }
        });

        reminderCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchActivities(2);
            }
        });

        todoCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchActivities(3);
            }
        });


    }

    private void switchActivities(int id) {
        Intent switchActivityIntent ;

        if(id ==1 ){
            switchActivityIntent = new Intent(this, activity_grades.class);
        }else if(id==2){
            switchActivityIntent = new Intent(this, activity_reminder.class);
        }else {
            switchActivityIntent = new Intent(this, activity_todo.class);
        }

        startActivity(switchActivityIntent);
    }
}