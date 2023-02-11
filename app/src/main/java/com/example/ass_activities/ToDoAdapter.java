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

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder>{

    private ArrayList<String> titles;
    private ArrayList<String> detailss;
    private ArrayList<String> dates;

    public ToDoAdapter(ArrayList<String> titles, ArrayList<String> details, ArrayList<String> dates) {
        this.titles = titles;
        this.detailss = details;
        this.dates = dates;
    }

    @NonNull
    @Override
    public ToDoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_card,
                parent,
                false);

        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull ToDoAdapter.ViewHolder holder, int position) {
        final CardView cardView = holder.cardView;
        final TextView txtTitle = cardView.findViewById(R.id.txtToDoTitle);
        TextView txtDetails = cardView.findViewById(R.id.txtToDoDetails);
        TextView txtDate = cardView.findViewById(R.id.txtToDoDate);
        Button btnDelete = cardView.findViewById(R.id.btnDeleteToDo);
        Button btnDone = cardView.findViewById(R.id.btnDoneToDo);

        txtTitle.setText(titles.get(position));
        txtDetails.setText(detailss.get(position));
        txtDate.setText(dates.get(position));

        final int currentPosition = position;
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titles.set(currentPosition, "Hello");
                notifyDataSetChanged();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItem(currentPosition);
            }
        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titles.set(currentPosition, "Done!");

                notifyDataSetChanged();
            }
        });
    }


    public void addItem(String title, String details, String date){
        titles.add(title);
        detailss.add(details);
        dates.add(date);
        notifyDataSetChanged();
    }


    private void removeItem(int position) {
        titles.remove(position);
        detailss.remove(position);
        dates.remove(position);
        notifyDataSetChanged();
    }



    @Override
    public int getItemCount() {
        return titles.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private CardView cardView;
        public ViewHolder(CardView cardView){
            super(cardView);
            this.cardView = cardView;
        }

    }

}
