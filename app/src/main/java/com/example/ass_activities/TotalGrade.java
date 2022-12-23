package com.example.ass_activities;

public class TotalGrade {

    private int achievedGrade;
    private int fullGrade;

    public TotalGrade(int achievedGrade, int fullGrade) {
        this.achievedGrade = achievedGrade;
        this.fullGrade = fullGrade;
    }

    public int getAchievedGrade() {
        return achievedGrade;
    }

    public void setAchievedGrade(int achievedGrade) {
        this.achievedGrade = achievedGrade;
    }

    public int getFullGrade() {
        return fullGrade;
    }

    public void setFullGrade(int fullGrade) {
        this.fullGrade = fullGrade;
    }

    public void addGrade(int newGrade, int newFullGrade){
        this.achievedGrade += newGrade;
        this.fullGrade += newFullGrade;
    }

    public void deleteGrade(int grade, int fullGrade){
        this.achievedGrade -= grade;
        this.fullGrade -= fullGrade;
    }


    @Override
    public String toString() {
        return "Total Grade: " + achievedGrade + " of " + "( " + fullGrade + " / 100 )";
    }
}
