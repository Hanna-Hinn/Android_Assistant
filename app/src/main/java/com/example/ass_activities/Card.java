package com.example.ass_activities;

public class Card {
    private String title;
    private String subject;
    private String details;
    private String date;

    public Card(String title, String details, String date) {
        this.title = title;
        this.details = details;
        this.date = date;
    }


    public Card() {
        this.title = "";
        this.subject= "";
        this.details = "";
        this.date = "";
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

    public void setSubject(String subject){
        this.subject = subject;
    }

    public String getSubject(){
        return this.subject;
    }

}