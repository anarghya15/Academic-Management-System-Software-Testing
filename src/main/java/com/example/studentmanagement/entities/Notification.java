package com.example.studentmanagement.entities;
import java.io.Serializable;

class Notification implements Serializable {
    private String message;
    private Student student;

    public Notification(String message, Student student) {
        this.message = message;
        this.student = student;
    }

    public int getStudentId() {
        return student.getId();
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "Notification for " + student.getName() + ": " + message;
    }
}