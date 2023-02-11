package com.example.ass_activities;

public class Grade {
    private String title;
    private String grade;
    private String realGrade;


    public Grade(String title, String grade, String realGrade) {
        this.title = title;
        this.grade = grade;
        this.realGrade = realGrade;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getRealGrade() {
        return realGrade;
    }

    public void setRealGrade(String realGrade) {
        this.realGrade = realGrade;
    }
}