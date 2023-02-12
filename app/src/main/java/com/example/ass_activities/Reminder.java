package com.example.ass_activities;

public class Reminder {
    private static int id=0;
    private String title;
    private String subject;
    private String details;
    private String date;

    public Reminder(String title, String subject, String details, String date) {
        this.title = title;
        this.subject = subject;
        this.details = details;
        this.date = date;
        id++;
    }

    public Reminder() {
//        id++;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
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
        return "Reminder{" +
                "title='" + title + '\'' +
                ", subject='" + subject + '\'' +
                ", details='" + details + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
