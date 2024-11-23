package com.example.studentmanagement;

import org.junit.jupiter.api.*;
import java.io.*;
import java.util.Scanner;

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
        Assertions.assertTrue(outputStreamCaptor.toString().contains("Invalid choice."));
    }

    @Test
    void testAdminMenuValidChoice() {
        simulateInput("admin\nadmin123\n1\n1\nJohn Doe\njohndoe@gmail.com\n0\n");
        Main.main(new String[]{});
        Assertions.assertTrue(outputStreamCaptor.toString().contains("Add Student"));
    }

    @Test
    void testInstructorMenuInvalidChoice() {
        simulateInput("instructor\ninst123\n99\n0\n");
        Main.main(new String[]{});
        Assertions.assertTrue(outputStreamCaptor.toString().contains("Invalid choice."));
    }

    @Test
    void testInstructorMenuValidChoice() {
        simulateInput("instructor\ninst123\n1\n1\n0\n");
        Main.main(new String[]{});
        Assertions.assertTrue(outputStreamCaptor.toString().contains("View Students in a course"));
    }

    @Test
    void testStudentMenuInvalidChoice() {
        simulateInput("student\nstud123\n99\n0\n");
        Main.main(new String[]{});
        Assertions.assertTrue(outputStreamCaptor.toString().contains("Invalid choice."));
    }

    @Test
    void testStudentMenuValidChoice() {
        simulateInput("student\nstud123\n1\n0\n");
        Main.main(new String[]{});
        Assertions.assertTrue(outputStreamCaptor.toString().contains("View Notifications"));
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

