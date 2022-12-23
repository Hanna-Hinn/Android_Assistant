package com.example.ass_activities;

public class ViewGrade {

    private String subject;
    private float grade;


    public ViewGrade(String subject) {
        this.subject = subject;
        this.grade = 0;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public float getGrade() {
        return grade;
    }

    public void setGrade(float grade) {
        this.grade = grade;
    }
}
