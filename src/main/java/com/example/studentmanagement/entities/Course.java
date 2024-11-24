package com.example.studentmanagement.entities;
import java.io.Serializable;
import java.util.List;

class Course implements Serializable {
    private int id;
    private String name;
    private int duration;
    private double fee;
    private double avgGrade;
    private List<Integer> prerequisites; // List of prerequisite course IDs

    public Course(int id, String name, int duration, double fee, List<Integer> prerequisites) {
        this.id = id;
        this.name = name;
        this.duration = duration;
        this.fee = fee;
        this.prerequisites = prerequisites;
    }
    

    public double getAvgGrade() {
        return avgGrade;
    }


    public void setAvgGrade(double avgGrade) {
        this.avgGrade = avgGrade;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public List<Integer> getPrerequisites() {
        return prerequisites;
    }

    public void addPrerequisites(int id) {
        this.prerequisites.add(id);
    }

    public double getFee() {
        return fee;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getDuration() {
        return duration;
    }

    @Override
    public String toString() {
        return "ID: " + id + ", Name: " + name + ", Duration: " + duration + " weeks, Fee: Rs." + fee;
    }
}