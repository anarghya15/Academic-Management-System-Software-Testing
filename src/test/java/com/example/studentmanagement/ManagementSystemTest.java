package com.example.studentmanagement;

import org.junit.jupiter.api.*;
import java.io.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ManagementSystemTest {
    private ManagementSystem managementSystem;

    @BeforeEach
    void setUp() {
        managementSystem = new ManagementSystem();
    }

    @Test
    void testAddStudentWithValidData() {
        String input = "1\nJohn Doe\njohn.doe@example.com\n"; // Simulated input
        simulateInput(input);

        managementSystem.addStudent(new Scanner(System.in));

        List<Student> students = managementSystem.getStudents(); // Assuming a getter for testing
        assertEquals(1, students.size());
        assertEquals("John Doe", students.get(0).getName());
        assertEquals("john.doe@example.com", students.get(0).getEmail());
    }

    @Test
    void testAddStudentWithInvalidEmail() {
        String input = "2\nJane Doe\ninvalid-email\n"; // Simulated input
        simulateInput(input);

        managementSystem.addStudent(new Scanner(System.in));

        List<Student> students = managementSystem.getStudents(); // Assuming a getter for testing
        assertEquals(0, students.size());
    }

    @Test
    void testAddCourse() {
        String input = "101\nMath 101\n12\n500.0\n"; // Simulated input
        simulateInput(input);

        managementSystem.addCourse(new Scanner(System.in));

        List<Course> courses = managementSystem.getCourses(); // Assuming a getter for testing
        assertEquals(1, courses.size());
        assertEquals("Math 101", courses.get(0).getName());
        assertEquals(12, courses.get(0).getDuration());
        assertEquals(500.0, courses.get(0).getFee());
    }

    @Test
    void testEnrollStudent() {
        // Adding sample student and course
        Student student = new Student(1, "John Doe", "john.doe@example.com");
        Course course = new Course(101, "Math 101", 12, 500.0, new ArrayList<>());
        managementSystem.getStudents().add(student);
        managementSystem.getCourses().add(course);

        String input = "1\n101\n"; // Simulated input
        simulateInput(input);

        managementSystem.enrollStudent(new Scanner(System.in));

        List<Enrollment> enrollments = managementSystem.getEnrollments(); // Assuming a getter for testing
        assertEquals(1, enrollments.size());
        assertEquals(student, enrollments.get(0).getStudent());
        assertEquals(course, enrollments.get(0).getCourse());
    }

    // Helper method to simulate user input
    private void simulateInput(String input) {
        System.setIn(new ByteArrayInputStream(input.getBytes()));
    }

    @AfterEach
    void tearDown() {
        System.setIn(System.in); // Reset System.in to its original state
    }
}


