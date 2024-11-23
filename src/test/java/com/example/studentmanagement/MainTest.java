package com.example.studentmanagement;

import org.junit.jupiter.api.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import static org.junit.jupiter.api.Assertions.*;

class MainTest {
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private final InputStream systemIn = System.in;
    private final PrintStream systemOut = System.out;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    void tearDown() {
        System.setOut(systemOut);
        System.setIn(systemIn);
    }

    private Scanner simulateInput(String data) {
        System.setIn(new ByteArrayInputStream(data.getBytes()));
        return new Scanner(System.in);
    }

    @Test
    void testAdminMenuInvalidChoice() {
        simulateInput("admin\nadmin123\n99\n0\n");
        Main.main(new String[]{});
        assertTrue(outputStreamCaptor.toString().contains("Invalid choice."));
    }

    @Test
    void testAdminMenuValidChoice() {
        simulateInput("admin\nadmin123\n1\n14\nPushpa\npushpa@gmail.com\n0\n");
        Main.main(new String[]{});
        assertTrue(outputStreamCaptor.toString().contains("Student added successfully!"));
    }

    @Test
    void testInstructorMenuInvalidChoice() {
        simulateInput("instructor\ninst123\n99\n0\n");
        Main.main(new String[]{});
        Assertions.assertTrue(outputStreamCaptor.toString().contains("Invalid choice."));
    }

    @Test
    void testInstructorMenuValidChoice() {
        simulateInput("instructor\ninst123\n1\n199\n0\n");
        Main.main(new String[]{});
        assertTrue(outputStreamCaptor.toString().contains("Course not found."));
    }

    @Test
    void testStudentMenuInvalidChoice() {
        simulateInput("student\nstud123\n99\n0\n");
        Main.main(new String[]{});
        assertTrue(outputStreamCaptor.toString().contains("Invalid choice."));
    }

    @Test
    void testStudentMenuValidChoice() {
        ManagementSystem system = new ManagementSystem();
        system.loadData();
        system.getCourses().add(new Course(1, "RS", 10, 1000, new ArrayList<>()));
        system.getStudents().add(new Student(1, "Anarghya", "anarghya@gmail.com"));
        system.saveData();

        simulateInput("student\nstud123\n5\n1\n1\n0\n");
        Main.main(new String[]{});
        assertTrue(outputStreamCaptor.toString().contains("Enrollment successful!"));
    }

    @Test
    void testAdminLogout() {
        simulateInput("admin\nadmin123\n0\n");
        Main.main(new String[]{});
        Assertions.assertTrue(outputStreamCaptor.toString().contains("Logging out..."));
    }

    @Test
    void testInstructorLogout() {
        simulateInput("instructor\ninst123\n0\n");
        Main.main(new String[]{});
        Assertions.assertTrue(outputStreamCaptor.toString().contains("Logging out..."));
    }

    @Test
    void testStudentLogout() {
        simulateInput("student\nstud123\n0\n");
        Main.main(new String[]{});
        Assertions.assertTrue(outputStreamCaptor.toString().contains("Logging out..."));
    }

    @Test
    void testInvalidLogin() {
        simulateInput("wrong\nwrong123\nadmin\nadmin123\n0\n");
        Main.main(new String[]{});
        Assertions.assertTrue(outputStreamCaptor.toString().contains("Invalid login. Try again."));
    }

    @Test
    void testRoleBasedMenuSwitch_Admin() {
        simulateInput("admin\nadmin123\n0\n");
        Main.main(new String[]{});
        Assertions.assertTrue(outputStreamCaptor.toString().contains("=== Main Menu (Admin) ==="));
    }

    @Test
    void testRoleBasedMenuSwitch_Instructor() {
        simulateInput("instructor\ninst123\n0\n");
        Main.main(new String[]{});
        Assertions.assertTrue(outputStreamCaptor.toString().contains("=== Main Menu (Instructor) ==="));
    }

    @Test
    void testRoleBasedMenuSwitch_Student() {
        simulateInput("student\nstud123\n0\n");
        Main.main(new String[]{});
        Assertions.assertTrue(outputStreamCaptor.toString().contains("=== Main Menu (Student) ==="));
    }
}

