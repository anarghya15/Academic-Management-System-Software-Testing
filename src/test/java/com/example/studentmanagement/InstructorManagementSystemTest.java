package com.example.studentmanagement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

import org.junit.jupiter.api.*;

import com.example.studentmanagement.entities.Course;
import com.example.studentmanagement.entities.Enrollment;
import com.example.studentmanagement.entities.Student;

public class InstructorManagementSystemTest {
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private final InputStream originalIn = System.in;
    private final PrintStream originalOut = System.out;

    private InstructorManagementSystem system;

    @BeforeEach
    void setUp() { 
        system = new InstructorManagementSystem();       
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
    void testAddGrade() {
        Student student = new Student(7, "Aishwarya", "Aishwarya@gmail.com");
        Course course = new Course(9, "TOC", 5, 700, new ArrayList<>());
        Enrollment enrollment = new Enrollment(2, student, course);
        system.getEnrollments().add(enrollment);

        Scanner scanner = simulateInput("7\n9\n85\n");
        system.addGrade(scanner);

        assertTrue(outputStreamCaptor.toString().contains("Grade added successfully!"));
        assertEquals(85, enrollment.getGrade());
    }

    @Test
    void testAddGrade_InvalidInput() {
        Student student = new Student(8, "Juli", "juli@gmail.com");
        Course course = new Course(10, "Physics", 5, 500, new ArrayList<>());
        Enrollment enrollment = new Enrollment(1, student, course);
        system.getEnrollments().add(enrollment);

        // Simulate invalid inputs
        Scanner scanner = simulateInput("8\n100\n50\n");
        system.addGrade(scanner);

        assertTrue(outputStreamCaptor.toString().contains("Enrollment not found. Cannot add grade."));
    }
    
    @Test
    public void testCalculateAverageGradeForCourse() {
        Course course = new Course(11, "Web Development", 8, 5000.0, new ArrayList<>());
        system.getCourses().add(course);

        Student student1 = new Student(9, "Rama", "rama@gmail.com");
        Student student2 = new Student(10, "Rajat", "rajat@gmail.com");

        Enrollment enrollment1 = new Enrollment(3, student1, course);
        enrollment1.setGrade(85.0);

        Enrollment enrollment2 = new Enrollment(4, student2, course);
        enrollment2.setGrade(90.0);

        system.getEnrollments().add(enrollment1);
        system.getEnrollments().add(enrollment2);

        // Simulate user input
        String simulatedInput = "11\n";
        Scanner scanner = simulateInput(simulatedInput);

        // Call the method
        system.calculateAverageGradeForCourse(scanner);

        // Validate output
        assertEquals(87.5, course.getAvgGrade());
    }
    
    @Test
    void testCalculateAverageGradeForCourse_NoGrades() {
        // Setup data
        Course course = new Course(12, "Math 101", 12, 500, new ArrayList<>());
        system.getCourses().add(course);

        String simulatedInput = "12\n";
        Scanner scanner = simulateInput(simulatedInput);
        system.calculateAverageGradeForCourse(scanner);

        String expectedOutput = "No grades available for this course.";
        assertTrue(outputStreamCaptor.toString().contains(expectedOutput));
        assertEquals(0, course.getAvgGrade());
    }

    @Test
    void testViewStudentsInCourse() {
        Student student = new Student(11, "Akash", "Akash@gmail.com");
        Student student2 = new Student(12, "Akshay", "akshay@gmail.com");
        Course course = new Course(12, "Chemistry", 16, 500, new ArrayList<>());
        Enrollment enrollment = new Enrollment(5, student, course);
        Enrollment enrollment2 = new Enrollment(6, student2, course);

        // system.getStudents().add(student);
        system.getCourses().add(course);
        system.getEnrollments().add(enrollment);
        system.getEnrollments().add(enrollment2);

        Scanner scanner = simulateInput("12\n");
        system.viewStudentsInCourse(scanner);

        assertTrue(outputStreamCaptor.toString().contains("Akash"));
        assertTrue(outputStreamCaptor.toString().contains("Akshay"));
    }

    @Test
    void testViewStudentsInCourse_InvalidId() {
        // Simulate invalid course ID input
        system.getCourses().add(new Course(1, "Math 101", 5, 500, new ArrayList<>()));
        Scanner scanner = simulateInput("2\n");
        system.viewStudentsInCourse(scanner);

        assertTrue(outputStreamCaptor.toString().contains("Course not found."));
    }
}
