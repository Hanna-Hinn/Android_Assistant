package com.example.ass_activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class activity_todo extends AppCompatActivity {

    Gson gson = new Gson();

    private Button btnAddTask;
    private ConstraintLayout layout;
    private EditText editTitle;
    private EditText editDetails;
    private TextView txtDate;
    private DatePicker calender;

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    public static final boolean FLAG = true;


    private ArrayList<Card> cards = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);



        setUpVariables();
        setUpSharedPreferences();
//        todoEditor.clear();
//        todoEditor.commit();
        checkData();


        //editDetails.setMovementMethod(new ScrollingMovementMethod());


        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(calender.getVisibility() == View.VISIBLE){
                    hideCalender();

                }else{
                    showCalender();
                    txtDate.setText("Close Calender");
                }
            }
        });



        btnAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String title = editTitle.getText().toString();
                String details = editDetails.getText().toString();

                // validate the input
                if (validate(title, details, txtDate.getText().toString())){

                    buildFromScratch(title, details, txtDate.getText().toString(), true); // build the new task
                    txtDate.setText("Chose Date");

                    // after adding the task, reset the values of the input fields
                    editTitle.setText("");
                    editDetails.setText("");

                }else{
                    Toast.makeText(activity_todo.this, "Fields can not be empty", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void checkData(){
        boolean flag = prefs.getBoolean("Todo_FLAG", false);
        if(flag){
            String strList = prefs.getString("cards", null);
            Type listType = new TypeToken<ArrayList<Card>>(){}.getType();
            cards = gson.fromJson(strList, listType);

            for(Card card : cards){
                buildFromScratch(card.getTitle(), card.getDetails(), card.getDate(), false);
            }
        }
    }

    private void setUpSharedPreferences(){
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        editor = prefs.edit();
    }

    private void setUpVariables(){
        txtDate = findViewById(R.id.txtDate);
        btnAddTask = findViewById(R.id.btnAddTask);
        editTitle = findViewById(R.id.txtTitle);
        editDetails = findViewById(R.id.txtDetails);
        calender = findViewById(R.id.calender);


    }

    private void showCalender(){
        AlphaAnimation fadeInAnimation = new AlphaAnimation(0, 1);
        fadeInAnimation.setDuration(300); // set the duration of the animation

        calender.setAnimation(fadeInAnimation);
        calender.setVisibility(View.VISIBLE);
    }

    private void hideCalender(){
        AlphaAnimation fadeOutAnimation = new AlphaAnimation(1, 0);
        fadeOutAnimation.setDuration(300); // set the duration of the animation

        calender.setAnimation(fadeOutAnimation);
        calender.setVisibility(View.GONE);

        String year = String.valueOf(calender.getYear());
        String month = String.valueOf(calender.getMonth());
        String day = String.valueOf(calender.getDayOfMonth());

        String date = year + " / " + month + " / " + day;
        txtDate.setText(date);
    }



    private void buildFromScratch(String title, String details, String date, boolean firstTime){
        LinearLayout layout = findViewById(R.id.tasksContainer);
        // Inflate the layout for the CardView
        LayoutInflater inflater = LayoutInflater.from(this);
        View cardView = inflater.inflate(R.layout.task_card, layout, false);


        // Add the CardView to the main activity layout
        layout.addView(cardView, layout.getChildCount());
        Card card = new Card(title, details, date);
        if(firstTime){
            cards.add(card);
        }

        // this code will declare the two text views inside the card.
        TextView txtTitle = cardView.findViewWithTag("tgTitle"); // this line means: give me any view inside the card with the tag(tgTitle)
        TextView txtDetails = cardView.findViewWithTag("tgDetails"); // this line means: give me any view inside the card with the tag(tgDetails)
        TextView txtCardDate = cardView.findViewWithTag("txtCardDate");
        // we select the button inside the card using the button tag
        Button btnDelete = cardView.findViewWithTag("tgBtnDelete");
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardView.setVisibility(View.GONE); // we simply set the visibility to gone for the card when the user press the x button
                for(int i = 0; i < cards.size(); i++){
                    if(cards.get(i).getTitle().equals(title) && cards.get(i).getDetails().equals(details) && cards.get(i).getDate().equals(date)){
                        cards.remove(i);
                        break;
                    }
                }
                cards.remove(card);
                save();
            }
        });

        // this line is important to make the TextView for details inside the card scrollable.
        txtDetails.setMovementMethod(new ScrollingMovementMethod());

        // adding the text from the parameters to the TextViews
        txtTitle.setText(title);
        txtDetails.setText(details);
        txtCardDate.setText("Due Date: " + date);


    }

    private boolean validate(String title, String details, String date){

        if (title.equals("") || details.equals("") || date.equals("Chose Date") || date.equals("Close Calender")){
            return false;
        }

        return true;
    }

    private void save(){

        String strList = gson.toJson(cards);
        editor.putBoolean("Todo_FLAG", FLAG);
        editor.putString("cards", strList);
        editor.apply();

    }

    @Override
    protected void onStop() {
        super.onStop();
        save();
    }
}