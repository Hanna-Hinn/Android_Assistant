package com.example.ass_activities;

public class Todo {
    private static int id=0;
    private String title;
    private String details;
    private String date;

    public Todo(String title, String details, String date) {
        this.title = title;
        this.details = details;
        this.date = date;
        id++;
    }

    public Todo() {
        title="";
        details="";
        date="";
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Todo: " + title + " , " + details + " , " + date;
    }
}
