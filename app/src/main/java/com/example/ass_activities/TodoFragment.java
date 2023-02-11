package com.example.ass_activities;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;
import static android.widget.Toast.LENGTH_LONG;

import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class TodoFragment extends Fragment {
    private FloatingActionButton fab;
    private RecyclerView recyclerView;

    private AlertDialog dialog;
    private EditText etTitle, etDetails;
    private DatePicker datePicker;

    private FirebaseDatabase database;
    private DatabaseReference ref;
    private String userID;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_todo, container, false);

        recyclerView = rootView.findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        fab = rootView.findViewById(R.id.fab);

        setUpDatabase();

        buildDialog();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    dialog.show();
            }
        });

        ArrayList<String> titles = new ArrayList<>();
        ArrayList<String> details = new ArrayList<>();
        ArrayList<String> dates = new ArrayList<>();


        //Read the Data from the database
        ref.child(userID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    Log.d(TAG, "Result: " + task.getResult().getValue().toString());
                }
            }
        });


        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ToDoAdapter adapter = new ToDoAdapter(titles, details, dates);
        recyclerView.setAdapter(adapter);

        return rootView;
    }

    private void setUpDatabase(){
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("todos");
        SharedPreferences Prefs = getActivity().getSharedPreferences("Auth", MODE_PRIVATE);
        userID = Prefs.getString("user",null);
    }


    public void addTodo(String title, String details, String date){
        ToDoAdapter adapter = (ToDoAdapter) recyclerView.getAdapter();
        adapter.addItem(title, details, date);
        //Write the data into the database
        Todo todo = new Todo(title,details,date);
        ref.child(userID).child(""+adapter.getItemCount()).child("title").setValue(todo.getTitle());
        ref.child(userID).child(""+adapter.getItemCount()).child("details").setValue(todo.getDetails());
        ref.child(userID).child(""+adapter.getItemCount()).child("date").setValue(todo.getDate());
    }

    private void buildDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getLayoutInflater().inflate(R.layout.todo_form, null);

        etTitle = view.findViewById(R.id.etTitle);
        etDetails = view.findViewById(R.id.etDetails);
        datePicker = view.findViewById(R.id.datePicker);

        etTitle.setText("");
        etDetails.setText("");

        builder.setView(view);
        builder.setTitle("Add Todo").setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String title = etTitle.getText().toString();
                String details = etDetails.getText().toString();

                if (validate(title, details)){
                    addTodo(title, details, getDate());

                }else{
                    Toast.makeText(getActivity(), "Title and Details can not be empty", LENGTH_LONG).show();
                }
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        dialog = builder.create();
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