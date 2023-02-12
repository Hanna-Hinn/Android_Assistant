package com.example.ass_activities;

import java.util.ArrayList;
import java.util.List;

public class SubjectGrade {
    private List<Mark> marks;
    private String total = "0";

    public SubjectGrade(String total, List<Mark> marks) {
        this.marks = marks;
        this.total = total;
    }
    public SubjectGrade() {
        marks= new ArrayList<>();
        total="0";
    }

    public List<Mark> getMarks() {
        return marks;
    }

    public void setMarks(List<Mark> marks) {
        this.marks = marks;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "SubjectGrade{" +
                "marks=" + marks +
                ", total='" + total + '\'' +
                '}';
    }
}
