package com.example.ass_activities;

import static android.content.ContentValues.TAG;

import android.nfc.Tag;
import android.provider.CalendarContract;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FirebaseDataBaseHelper {
    private FirebaseDatabase mDataBase;
    private DatabaseReference mRef;
    private List<Todo> data = new ArrayList<>();
    private List<Reminder> reminders = new ArrayList<>();
    private HashMap<String, SubjectGrade> grades = new HashMap<>();
    private List<Mark> marks = new ArrayList<>();

    public interface DataStatus {
        void TodosIsLoaded(List<Todo> data, List<String> keys);

        void ReminderIsLoaded(List<Reminder> data, List<String> keys);

        void GradeIsLoaded(HashMap<String, SubjectGrade> data);

        void SubjectGradesIsLoaded(List<Mark> data, List<String> keys);

    }

    public FirebaseDataBaseHelper() {
        mDataBase = FirebaseDatabase.getInstance();
        mRef = mDataBase.getReference();
    }

    public void readTodos(final DataStatus dataStatus, String userID) {
        mRef.child("todos").child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                data.clear();
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : snapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    Todo todo = keyNode.getValue(Todo.class);
                    data.add(todo);
                }
                dataStatus.TodosIsLoaded(data, keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void readReminder(final DataStatus dataStatus, String userID) {
        mRef.child("reminders").child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reminders.clear();
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : snapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    Reminder reminder = keyNode.getValue(Reminder.class);
                    reminders.add(reminder);
                }
                dataStatus.ReminderIsLoaded(reminders, keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void readGrades(final DataStatus dataStatus, String userID) {
        mRef.child("grades").child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                grades.clear();
                for (DataSnapshot keyNode : snapshot.getChildren()) {

                    SubjectGrade grade = keyNode.getValue(SubjectGrade.class);
                    grades.put(keyNode.getKey(), grade);

                }
                dataStatus.GradeIsLoaded(grades);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void readSubjectGrades (final DataStatus dataStatus, String userID, String subject){
        mRef.child("grades").child(userID).child(subject).child("marks").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                marks.clear();
                List<String> keys = new ArrayList<>();
                for(DataSnapshot keyNode : snapshot.getChildren()){
                    keys.add(keyNode.getKey());
                    Mark mark = keyNode.getValue(Mark.class);
                    marks.add(mark);
                }
                dataStatus.SubjectGradesIsLoaded(marks, keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}
