package com.example.studentmanagement;

import org.junit.jupiter.api.*;

import com.example.studentmanagement.entities.Course;
import com.example.studentmanagement.entities.Enrollment;
import com.example.studentmanagement.entities.Student;

import java.io.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class AdminManagementSystemTest {
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private final InputStream originalIn = System.in;
    private final PrintStream originalOut = System.out;

    private AdminManagementSystem system;

    @BeforeEach
    void setUp() { 
        system = new AdminManagementSystem();       
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
    public void testAddPrerequisiteSuccess() {
        Course course1 = new Course(1, "Java Programming", 8, 5000.0, new ArrayList<>());
        Course course2 = new Course(2, "Advanced Java", 10, 7000.0, new ArrayList<>());

        system.getCourses().add(course1);
        system.getCourses().add(course2);

        // Simulate user input
        String simulatedInput = "2\n1\n"; 
        Scanner scanner = simulateInput(simulatedInput);
        system.addPrerequisite(scanner);

        // Validate output
        String output = outputStreamCaptor.toString();
        assertTrue(output.contains("Prerequisite added successfully"));
        assertTrue(course2.getPrerequisites().contains(1));
    }

    @Test
    public void testAddPrerequisiteFailure() {
        // Simulate user input for non-existent courses
        String simulatedInput = "999\n888\n";
        Scanner scanner = simulateInput(simulatedInput);
        system.addPrerequisite(scanner);

        // Validate output
        String output = outputStreamCaptor.toString();
        assertTrue(output.contains("Course not found"));
    }

    @Test
    void testAddStudent_ValidEmail() {
        String simulatedInput = "1\nAnarghya\nanarghya@gmail.com\n";
        Scanner scanner = simulateInput(simulatedInput);
        int studentsSize = system.getStudents().size();
        int notifSize = system.getNotifications().size();
        
        system.addStudent(scanner);

        String expectedOutput = "Student added successfully!";
        assertTrue(outputStreamCaptor.toString().contains(expectedOutput));

        assertEquals(studentsSize + 1, system.getStudents().size());
        assertEquals("Anarghya", system.getStudents().get(system.getStudents().size() - 1).getName());
        assertEquals(notifSize + 1, system.getNotifications().size());
    }

    @Test
    void testAddStudent_InvalidEmail() {
        String simulatedInput = "2\nAnamika\ninvalid-email\n";
        Scanner scanner = simulateInput(simulatedInput);
        int studentsSize = system.getStudents().size();
        int notifSize = system.getNotifications().size();
        system.addStudent(scanner);

        String expectedOutput = "Invalid email format. Student not added.";
        assertTrue(outputStreamCaptor.toString().contains(expectedOutput));

        assertEquals(studentsSize, system.getStudents().size());
        assertEquals(notifSize, system.getNotifications().size());
    }

    @Test
    void testAddCourse() {
        String simulatedInput = "3\nMaths\n12\n500\n";
        Scanner scanner = simulateInput(simulatedInput);
        int courseSize = system.getCourses().size();
        system.addCourse(scanner);

        String expectedOutput = "Course added successfully!";
        assertTrue(outputStreamCaptor.toString().contains(expectedOutput));

        assertEquals(courseSize + 1, system.getCourses().size());
        assertEquals("Maths", system.getCourses().get(system.getCourses().size() - 1).getName());
    }

    @Test
    void testAddCourse_InvalidInput() {
        String simulatedInput = "abc\nMaths\n12\n500\n";
        Scanner scanner = simulateInput(simulatedInput);
        assertThrows(InputMismatchException.class, () ->{
            system.addCourse(scanner);
        });
    }

    @Test
    void testGenerateOutstandingReport() {
        Student student1 = new Student(2, "Nisha", "nisha@gmail.com");
        Course course1 = new Course(4, "SADP", 10, 500, new ArrayList<>());
        Enrollment enrollment = new Enrollment(1, student1, course1);

        system.getEnrollments().add(enrollment);

        system.generateOutstandingReport();
        assertTrue(outputStreamCaptor.toString().contains("Outstanding: Rs.500"));

    }

    @Test
    void testGenerateOutstandingReport_NoResult() {
        Student student1 = new Student(2, "Nisha", "nisha@gmail.com");
        Course course1 = new Course(4, "SADP", 10, 500, new ArrayList<>());
        Enrollment enrollment = new Enrollment(1, student1, course1);
        enrollment.setBalanceAmt(0);
        enrollment.setFeePaid(true);

        system.getEnrollments().add(enrollment);

        system.generateOutstandingReport();
        assertFalse(outputStreamCaptor.toString().contains("Outstanding: Rs.500"));

    }

    @Test
    void testUpdateStudent() {
        // Setup data
        Student student = new Student(3, "Rahul", "rahul@gmail.com");
        system.getStudents().add(student);

        String simulatedInput = "3\nRahul S\nrahuls@gmail.com\n";
        Scanner scanner = simulateInput(simulatedInput);
        system.updateStudent(scanner);

        String expectedOutput = "Student updated successfully!";
        assertTrue(outputStreamCaptor.toString().contains(expectedOutput));

        assertEquals("Rahul S", student.getName());
        assertEquals("rahuls@gmail.com", student.getEmail());
    }

    @Test
    void testDeleteStudent() {
        // Setup data
        Student student = new Student(4, "Abhay", "abhay@gmail.com");
        system.getStudents().add(student);
        int studentSize = system.getStudents().size();

        String simulatedInput = "4\n";
        Scanner scanner = simulateInput(simulatedInput);
        system.deleteStudent(scanner);

        String expectedOutput = "Student deleted successfully.";
        assertTrue(outputStreamCaptor.toString().contains(expectedOutput));

        assertEquals(studentSize - 1, system.getStudents().size());
    }

    @Test
    void testFilterCoursesByDuration() {
        Course course1 = new Course(5, "Algorithms", 5, 500, new ArrayList<>());
        Course course2 = new Course(6, "OS", 10, 700, new ArrayList<>());
        system.getCourses().add(course1);
        system.getCourses().add(course2);

        Scanner scanner = simulateInput("6\n");
        system.filterCoursesByDuration(scanner);

        String output = outputStreamCaptor.toString();
        assertFalse(output.contains("Algorithms"));
        assertTrue(output.contains("OS"));
    }

    @Test
    public void testExportDataAsTextSuccess() throws IOException {
        Student student = new Student(4, "Abhay", "abhay@gmail.com");
        system.getStudents().add(student);
        String filename = "test_export.txt";
        system.exportDataAsText(filename);

        File file = new File(filename);
        assertTrue(file.exists());
        assertTrue(file.length() > 0);

        file.delete();
    }

    @Test
    void testSearchStudent() {
        Student student = new Student(5, "Vikrant", "vikrant@gmail.com");
        system.getStudents().add(student);

        Scanner scanner = simulateInput("5\n");
        Student s = system.searchStudent(scanner);

        assertNotNull(s);
        assertTrue(s.getName().contains("Vikrant"));
    }

    @Test
    void testSearchStudent_InvalidId() {
        // Simulate invalid student ID input
        system.getStudents().add(new Student(6, "Amar", "amar@gmail.com.com"));
        Scanner scanner = simulateInput("100\n");
        Student s = system.searchStudent(scanner);

        assertNull(s);
    }

    @Test
    void testSearchCourse() {
        Course course = new Course(7, "SPE", 15, 500, new ArrayList<>());
        system.getCourses().add(course);

        Scanner scanner = simulateInput("7\n");
        Course c = system.searchCourse(scanner);

        assertNotNull(c);
        assertTrue(c.getName().contains("SPE"));
    }    

    @Test
    void testSearchCourse_InvalidId() {
        // Simulate invalid course ID input
        system.getCourses().add(
                new Course(8, "Math 101", 9, 500, new ArrayList<>())
        );
        Scanner scanner = simulateInput("100\n");
        Course c = system.searchCourse(scanner);

        assertNull(c);
    }
    
}
