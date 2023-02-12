package com.example.ass_activities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class GradeAdapter extends RecyclerView.Adapter<GradeAdapter.ViewHolder>{

    private ArrayList<String> titles;
    private ArrayList<String> grades;
    private ArrayList<String> realGrades;

    public GradeAdapter(ArrayList<String> titles, ArrayList<String> grades, ArrayList<String> realGrades) {
        this.titles = titles;
        this.grades = grades;
        this.realGrades = realGrades;
    }

    @NonNull
    @Override
    public GradeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.grade_card,
                parent,
                false);

        return new GradeAdapter.ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull GradeAdapter.ViewHolder holder, int position) {
        final CardView cardView = holder.cardView;
        final TextView txtGradeTitle = cardView.findViewById(R.id.txtGradeTitle);
        final TextView txtGrade = cardView.findViewById(R.id.txtGrade);
        final TextView txtRealGrade = cardView.findViewById(R.id.txtRealGrade);
        final Button btnDeleteGrade = cardView.findViewById(R.id.btnDeleteGrade);

        txtGradeTitle.setText(titles.get(position));

        String grade = "Grade: " + grades.get(position);
        txtGrade.setText(grade);

        String realGrade = "Real Grade: " + realGrades.get(position);
        txtRealGrade.setText(realGrade);

        final int currentPosition = position;


        btnDeleteGrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String realGrade = realGrades.get(currentPosition).split("/")[0].split(" ")[0];
//                String realFullGrade = realGrades.get(currentPosition).split("/")[1].split(" ")[1];

                removeItem(currentPosition);
//                activity_subject_grades.updateTotal(realGrade, realFullGrade, false);
            }
        });

    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    public void addItem(String title, String grade, String realGrade){
        titles.add(title);
        grades.add(grade);
        realGrades.add(realGrade);
        notifyDataSetChanged();
    }


    private void removeItem(int position) {
        titles.remove(position);
        grades.remove(position);
        realGrades.remove(position);
        activity_subject_grades.deleteFromDatabase(position);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private CardView cardView;
        public ViewHolder(CardView cardView){
            super(cardView);
            this.cardView = cardView;
        }

    }
}
