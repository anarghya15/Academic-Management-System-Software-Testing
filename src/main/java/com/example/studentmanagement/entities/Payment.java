package com.example.studentmanagement.entities;
import java.io.Serializable;
import java.time.LocalDate;

class Payment implements Serializable {
    private Enrollment enrollment;
    private double amountPaid;
    private String date;

    public Payment(Enrollment enrollment, double fee) {
        this.enrollment = enrollment;
        this.amountPaid = fee;
        this.date = LocalDate.now().toString();
    }

    @Override
    public String toString() {
        return "Payment [Student: " + enrollment.getStudent().getName() + ", Course: " + enrollment.getCourse().getName() + ", Paid: " + amountPaid
                + "Date of Payment: " + date + "]";
    }
}