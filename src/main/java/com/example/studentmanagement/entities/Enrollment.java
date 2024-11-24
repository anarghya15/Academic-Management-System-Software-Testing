package com.example.studentmanagement.entities;
import java.io.Serializable;

public class Enrollment implements Serializable {
    private int id;
    private Student student;
    private Course course;
    private double grade;
    private double balanceAmt;
    private boolean feePaid;

    public Enrollment(int id, Student student, Course course) {
        this.id = id;
        this.student = student;
        this.course = course;
        this.balanceAmt = course.getFee();
        this.feePaid = false;
    }

    public Student getStudent() {
        return student;
    }

    public Course getCourse() {
        return course;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public boolean isFeePaid() {
        return feePaid;
    }

    public void setFeePaid(boolean feePaid) {
        this.feePaid = feePaid;
    }
    
    @Override
    public String toString() {
        return "Student: " + student + ", Course: " + course;
    }

    public int getId() {
        return id;
    }

    public double getBalanceAmt() {
        return balanceAmt;
    }

    public void setBalanceAmt(double amt) {
        this.balanceAmt = amt;
    }   

}