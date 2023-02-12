package com.example.ass_activities;

public class Mark {
    private String full_grade;
    private String grade;
    private String real_full;
    private String real_grade;
    private String title;

    public Mark(String full_grade, String grade, String real_full, String real_grade, String title) {
        this.full_grade = full_grade;
        this.grade = grade;
        this.real_full = real_full;
        this.real_grade = real_grade;
        this.title = title;
    }

    public Mark() {
    }

    public String getFull_grade() {
        return full_grade;
    }

    public void setFull_grade(String full_grade) {
        this.full_grade = full_grade;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getReal_full() {
        return real_full;
    }

    public void setReal_full(String real_full) {
        this.real_full = real_full;
    }

    public String getReal_grade() {
        return real_grade;
    }

    public void setReal_grade(String real_grade) {
        this.real_grade = real_grade;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Mark{" +
                "full_grade='" + full_grade + '\'' +
                ", grade='" + grade + '\'' +
                ", real_full='" + real_full + '\'' +
                ", real_grade='" + real_grade + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
