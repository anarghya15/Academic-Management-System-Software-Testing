package com.example.studentmanagement.entities;
import java.io.Serializable;

class Achievement implements Serializable {
    private Student student;
    private String title;

    public Achievement(Student student, String title) {
        this.student = student;
        this.title = title;
    }

    @Override
    public String toString() {
        return "Achievement [Student: " + student.getName() + ", Title: " + title + "]";
    }
}