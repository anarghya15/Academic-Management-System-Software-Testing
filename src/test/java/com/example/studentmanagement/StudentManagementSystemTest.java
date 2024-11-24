package com.example.studentmanagement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.studentmanagement.entities.Course;
import com.example.studentmanagement.entities.Enrollment;
import com.example.studentmanagement.entities.Student;

public class StudentManagementSystemTest {
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private final InputStream originalIn = System.in;
    private final PrintStream originalOut = System.out;

    private StudentManagementSystem system;

    @BeforeEach
    void setUp() { 
        system = new StudentManagementSystem();       
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    void tearDownInputOutput() {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    private Scanner simulateInput(String input) {
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        return new Scanner(System.in);
    }

    @Test
    void testMakePayment() {
        Student student = new Student(1, "Divyam", "divyam@gmail.com");
        Course course = new Course(1, "COA", 5, 500, new ArrayList<>());
        Enrollment enrollment = new Enrollment(1, student, course);
        
        system.getEnrollments().add(enrollment);

        Scanner scanner = simulateInput("1\n200\n");
        system.makePayment(scanner);

        assertTrue(outputStreamCaptor.toString().contains("Payment made successfully"));
        assertEquals(300, enrollment.getBalanceAmt());
    }

    @Test
    void testMakePaymentInvalidInput() {
        Student student = new Student(1, "Divyam", "divyam@gmail.com");
        Course course = new Course(1, "COA", 5, 500, new ArrayList<>());
        Enrollment enrollment = new Enrollment(1, student, course);
        system.getEnrollments().add(enrollment);

        // Simulate invalid inputs for Enrollment ID
        Scanner scanner = simulateInput("5\n200\n");
        system.makePayment(scanner);

        assertTrue(outputStreamCaptor.toString().contains("Invalid Enrollment ID or fee already paid."));
    }
    

    @Test
    void testEnrollStudent_Success() {
        // Setup data
        Student student = new Student(13, "Jahnavi", "jahnavi@gmail.com");
        Course course = new Course(13, "FSL", 12, 500, new ArrayList<>());
        system.getStudents().add(student);
        system.getCourses().add(course);
        int enrollmentSize = system.getEnrollments().size();

        String simulatedInput = "13\n13\n";
        Scanner scanner = simulateInput(simulatedInput);
        system.enrollStudent(scanner);

        String expectedOutput = "Enrollment successful!";
        assertTrue(outputStreamCaptor.toString().contains(expectedOutput));

        assertEquals(enrollmentSize + 1, system.getEnrollments().size());
    }

    @Test
    void testEnrollStudent_MissingPrerequisite() {
        // Setup data
        Student student = new Student(1, "Naman", "naman@gmail.com");
        Course course = new Course(13, "FSL", 12, 500, new ArrayList<>());
        Course course2 = new Course(14, "Advanced FSL", 12, 500, Arrays.asList(13)); // Requires course 100
        
        system.getStudents().add(student);
        system.getCourses().add(course);
        system.getCourses().add(course2);

        Enrollment enrollment = new Enrollment(1, student, course);
        enrollment.setGrade(20);
        system.getEnrollments().add(enrollment);

        int enrollmentSize = system.getEnrollments().size(); 

        String simulatedInput = "1\n14\n";
        Scanner scanner = simulateInput(simulatedInput);
        system.enrollStudent(scanner);

        String expectedOutput = "Prerequisites not met. Enrollment failed.";
        assertTrue(outputStreamCaptor.toString().contains(expectedOutput));

        assertEquals(enrollmentSize, system.getEnrollments().size());
    }

    @Test
    void testViewCoursesForStudent() {
        Student student = new Student(1, "Sam", "sam@gmail.com");
        Course course = new Course(1, "Software Testing", 5, 500, new ArrayList<>());
        Enrollment enrollment = new Enrollment(1, student, course);

        system.getStudents().add(student);
        system.getCourses().add(course);
        system.getEnrollments().add(enrollment);

        Scanner scanner = simulateInput("1\n");
        system.viewCoursesForStudent(scanner);

        assertTrue(outputStreamCaptor.toString().contains("Software Testing"));
    }

    @Test
    void testViewCoursesForStudent_InvalidId() {
        // Simulate invalid student ID input
        Student student = new Student(1, "Sam", "sam@gmail.com");
        system.getStudents().add(student);

        Scanner scanner = simulateInput("2\n");
        system.viewCoursesForStudent(scanner);

        assertTrue(outputStreamCaptor.toString().contains("Student not found."));
    }

    @Test
    void testViewGradesForStudent() {
        Student student = new Student(1, "Anagha", "anagha@gmail.com");
        Course course = new Course(1, "Machine Learning", 5, 500, new ArrayList<>());
        Enrollment enrollment = new Enrollment(1, student, course);
        enrollment.setGrade(85);

        system.getEnrollments().add(enrollment);

        Scanner scanner = simulateInput("1\n");
        system.viewGradesForStudent(scanner);

        assertTrue(outputStreamCaptor.toString().contains("Machine Learning"));
        assertTrue(outputStreamCaptor.toString().contains("85"));
    }    

    @Test
    void testViewGradesForStudent_InvalidId() {
        // Simulate invalid student ID input
        Student student = new Student(1, "Anagha", "anagha@gmail.com");
        system.getStudents().add(student);

        Scanner scanner = simulateInput("100\n");
        system.viewGradesForStudent(scanner);

        assertTrue(outputStreamCaptor.toString().contains("Grades for student 100:"));
        assertFalse(outputStreamCaptor.toString().contains("Course: "));
    }
}
