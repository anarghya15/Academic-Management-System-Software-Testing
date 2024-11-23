package com.example.studentmanagement;

import org.junit.jupiter.api.*;
import java.io.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class ManagementSystemTest {

    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private final InputStream originalIn = System.in;
    private final PrintStream originalOut = System.out;

    private ManagementSystem system;

    @BeforeEach
    void setUp() {
        system = new ManagementSystem();
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    private Scanner simulateInput(String input) {
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        return new Scanner(System.in);
    }

    @Test
    void testAddStudent_ValidEmail() {
        String simulatedInput = "1\nJohn Doe\njohn.doe@example.com\n";
        Scanner scanner = simulateInput(simulatedInput);
        system.addStudent(scanner);

        String expectedOutput = "Student added successfully!";
        assertTrue(outputStreamCaptor.toString().contains(expectedOutput));

        assertEquals(1, system.getStudents().size());
        assertEquals("John Doe", system.getStudents().get(0).getName());
    }

    @Test
    void testAddStudent_InvalidEmail() {
        String simulatedInput = "1\nJohn Doe\ninvalid-email\n";
        Scanner scanner = simulateInput(simulatedInput);
        system.addStudent(scanner);

        String expectedOutput = "Invalid email format. Student not added.";
        assertTrue(outputStreamCaptor.toString().contains(expectedOutput));

        assertEquals(0, system.getStudents().size());
    }

    @Test
    void testAddCourse() {
        String simulatedInput = "101\nMath 101\n12\n500\n";
        Scanner scanner = simulateInput(simulatedInput);
        system.addCourse(scanner);

        String expectedOutput = "Course added successfully!";
        assertTrue(outputStreamCaptor.toString().contains(expectedOutput));

        assertEquals(1, system.getCourses().size());
        assertEquals("Math 101", system.getCourses().get(0).getName());
    }

    @Test
    void testEnrollStudent_Success() {
        // Setup data
        Student student = new Student(1, "John Doe", "john.doe@example.com");
        Course course = new Course(101, "Math 101", 12, 500, new ArrayList<>());
        system.getStudents().add(student);
        system.getCourses().add(course);

        String simulatedInput = "1\n101\n";
        Scanner scanner = simulateInput(simulatedInput);
        system.enrollStudent(scanner);

        String expectedOutput = "Enrollment successful!";
        assertTrue(outputStreamCaptor.toString().contains(expectedOutput));

        assertEquals(1, system.getEnrollments().size());
    }

    @Test
    void testEnrollStudent_MissingPrerequisite() {
        // Setup data
        Student student = new Student(1, "John Doe", "john.doe@example.com");
        Course course = new Course(101, "Advanced Math", 12, 500, Arrays.asList(100)); // Requires course 100
        system.getStudents().add(student);
        system.getCourses().add(course);

        String simulatedInput = "1\n101\n";
        Scanner scanner = simulateInput(simulatedInput);
        system.enrollStudent(scanner);

        String expectedOutput = "Prerequisites not met. Enrollment failed.";
        assertTrue(outputStreamCaptor.toString().contains(expectedOutput));

        assertEquals(0, system.getEnrollments().size());
    }

    @Test
    void testUpdateStudent() {
        // Setup data
        Student student = new Student(1, "John Doe", "john.doe@example.com");
        system.getStudents().add(student);

        String simulatedInput = "1\nJane Doe\njane.doe@example.com\n";
        Scanner scanner = simulateInput(simulatedInput);
        system.updateStudent(scanner);

        String expectedOutput = "Student updated successfully!";
        assertTrue(outputStreamCaptor.toString().contains(expectedOutput));

        assertEquals("Jane Doe", student.getName());
        assertEquals("jane.doe@example.com", student.getEmail());
    }

    @Test
    void testDeleteStudent() {
        // Setup data
        Student student = new Student(1, "John Doe", "john.doe@example.com");
        system.getStudents().add(student);

        String simulatedInput = "1\n";
        Scanner scanner = simulateInput(simulatedInput);
        system.deleteStudent(scanner);

        String expectedOutput = "Student deleted successfully.";
        assertTrue(outputStreamCaptor.toString().contains(expectedOutput));

        assertTrue(system.getStudents().isEmpty());
    }

    @Test
    void testCalculateAverageGradeForCourse_NoGrades() {
        // Setup data
        Course course = new Course(101, "Math 101", 12, 500, new ArrayList<>());
        system.getCourses().add(course);

        String simulatedInput = "101\n";
        Scanner scanner = simulateInput(simulatedInput);
        system.calculateAverageGradeForCourse(scanner);

        String expectedOutput = "No grades available for this course.";
        assertTrue(outputStreamCaptor.toString().contains(expectedOutput));
    }

    @Test
    public void testCalculateAverageGradeForCourse() {
        Course course = new Course(201, "Java Basics", 8, 5000.0, new ArrayList<>());
        system.getCourses().add(course);

        Student student1 = new Student(101, "John Doe", "john@example.com");
        Student student2 = new Student(102, "Jane Doe", "jane@example.com");

        Enrollment enrollment1 = new Enrollment(1, student1, course);
        enrollment1.setGrade(85.0);

        Enrollment enrollment2 = new Enrollment(2, student2, course);
        enrollment2.setGrade(90.0);

        system.getEnrollments().add(enrollment1);
        system.getEnrollments().add(enrollment2);

        // Simulate user input
        String simulatedInput = "201\n";
        Scanner scanner = simulateInput(simulatedInput);

        // Call the method
        system.calculateAverageGradeForCourse(scanner);

        // Validate output
        String output = outputStreamCaptor.toString();
        assertTrue(output.contains("87.5"));
    }

    @Test
    public void testExportDataAsTextSuccess() throws IOException {
        system.getStudents().add(new Student(101, "John Doe", "john@example.com"));
        system.getCourses().add(new Course(201, "Java Basics", 8, 5000.0, new ArrayList<>()));

        String filename = "test_export.txt";
        system.exportDataAsText(filename);

        File file = new File(filename);
        assertTrue(file.exists());
        assertTrue(file.length() > 0);

        // Clean up test file
        file.delete();
    }

    @Test
    public void testAddPrerequisiteSuccess() {
        ManagementSystem system = new ManagementSystem();
        Course course1 = new Course(101, "Java Basics", 8, 5000.0, new ArrayList<>());
        Course course2 = new Course(102, "Advanced Java", 10, 7000.0, new ArrayList<>());

        system.getCourses().add(course1);
        system.getCourses().add(course2);

        // Simulate user input
        String simulatedInput = "102\n101\n"; // Add "Java Basics" as a prerequisite for "Advanced Java"
        Scanner scanner = simulateInput(simulatedInput);
        system.addPrerequisite(scanner);

        // Validate output
        String output = outputStreamCaptor.toString();
        assertTrue(output.contains("Prerequisite added successfully"));
        assertTrue(course2.getPrerequisites().contains(101));
    }

    @Test
    public void testAddPrerequisiteFailure() {
        ManagementSystem system = new ManagementSystem();

        // Simulate user input for non-existent courses
        String simulatedInput = "999\n888\n";
        Scanner scanner = simulateInput(simulatedInput);
        system.addPrerequisite(scanner);

        // Validate output
        String output = outputStreamCaptor.toString();
        assertTrue(output.contains("Course not found"));
    }

    @Test
    void testGenerateOutstandingReport() {
        Student student1 = new Student(1, "John Doe", "john@example.com");
        Course course1 = new Course(1, "Math 101", 10, 500, new ArrayList<>());
        Enrollment enrollment = new Enrollment(1, student1, course1);

        List<Enrollment> enrollments = List.of(enrollment);
        enrollment.setBalanceAmt(500); // Outstanding fee
        system.setEnrollments(enrollments);

        system.generateOutstandingReport();
        assertTrue(outputStreamCaptor.toString().contains("Outstanding: $500"));
    }

    @Test
    void testFilterCoursesByDuration() {
        Course course1 = new Course(1, "Math 101", 5, 500, new ArrayList<>());
        Course course2 = new Course(2, "Science 101", 10, 700, new ArrayList<>());
        system.setCourses(List.of(course1, course2));

        Scanner scanner = simulateInput("6\n");
        system.filterCoursesByDuration(scanner);

        String output = outputStreamCaptor.toString();
        assertFalse(output.contains("Math 101"));
        assertTrue(output.contains("Science 101"));
    }

    @Test
    void testSearchStudent() {
        Student student = new Student(1, "John Doe", "john@example.com");
        system.setStudents(List.of(student));

        Scanner scanner = simulateInput("1\n");
        system.searchStudent(scanner);

        assertTrue(outputStreamCaptor.toString().contains("John Doe"));
    }

    @Test
    void testSearchCourse() {
        Course course = new Course(1, "Math 101", 5, 500, new ArrayList<>());
        system.setCourses(List.of(course));

        Scanner scanner = simulateInput("1\n");
        system.searchCourse(scanner);

        assertTrue(outputStreamCaptor.toString().contains("Math 101"));
    }

    @Test
    void testSearchStudentInvalidId() {
        // Simulate invalid student ID input
        system.setStudents(List.of(
                new Student(1, "John Doe", "john@example.com")
        ));
        Scanner scanner = simulateInput("2\n");
        system.searchStudent(scanner);

        assertTrue(outputStreamCaptor.toString().contains("Student not found."));
    }

    @Test
    void testSearchCourseInvalidId() {
        // Simulate invalid course ID input
        system.setCourses(List.of(
                new Course(1, "Math 101", 5, 500, new ArrayList<>())
        ));
        Scanner scanner = simulateInput("2\n");
        system.searchCourse(scanner);

        assertTrue(outputStreamCaptor.toString().contains("Course not found."));
    }

    @Test
    void testAddGrade() {
        Student student = new Student(1, "John Doe", "john@example.com");
        Course course = new Course(1, "Math 101", 5, 500, new ArrayList<>());
        Enrollment enrollment = new Enrollment(1, student, course);
        system.setEnrollments(List.of(enrollment));

        Scanner scanner = simulateInput("1\n1\n85\n");
        system.addGrade(scanner);

        assertTrue(outputStreamCaptor.toString().contains("Grade added successfully!"));
        assertEquals(85, enrollment.getGrade());
    }

    @Test
    void testAddGradeInvalidInput() {
        Student student = new Student(1, "John Doe", "john@example.com");
        Course course = new Course(1, "Math 101", 5, 500, new ArrayList<>());
        Enrollment enrollment = new Enrollment(1, student, course);
        system.setEnrollments(List.of(enrollment));

        // Simulate invalid inputs
        Scanner scanner = simulateInput("1\n2\n50\n");
        system.addGrade(scanner);

        assertTrue(outputStreamCaptor.toString().contains("Enrollment not found. Cannot add grade."));
    }

    @Test
    void testViewStudentsInCourse() {
        Student student = new Student(1, "John Doe", "john@example.com");
        Course course = new Course(1, "Math 101", 5, 500, new ArrayList<>());
        Enrollment enrollment = new Enrollment(1, student, course);

        system.setStudents(List.of(student));
        system.setCourses(List.of(course));
        system.setEnrollments(List.of(enrollment));

        Scanner scanner = simulateInput("1\n");
        system.viewStudentsInCourse(scanner);

        assertTrue(outputStreamCaptor.toString().contains("John Doe"));
    }

    @Test
    void testMakePayment() {
        Student student = new Student(1, "John Doe", "john@example.com");
        Course course = new Course(1, "Math 101", 5, 500, new ArrayList<>());
        Enrollment enrollment = new Enrollment(1, student, course);
        enrollment.setBalanceAmt(500); // Outstanding fee
        system.setEnrollments(List.of(enrollment));

        Scanner scanner = simulateInput("1\n200\n");
        system.makePayment(scanner);

        assertTrue(outputStreamCaptor.toString().contains("Payment made successfully"));
        assertEquals(300, enrollment.getBalanceAmt());
    }

    @Test
    void testViewCoursesForStudent() {
        Student student = new Student(1, "John Doe", "john@example.com");
        Course course = new Course(1, "Math 101", 5, 500, new ArrayList<>());
        Enrollment enrollment = new Enrollment(1, student, course);

        system.setStudents(List.of(student));
        system.setCourses(List.of(course));
        system.setEnrollments(List.of(enrollment));

        Scanner scanner = simulateInput("1\n");
        system.viewCoursesForStudent(scanner);

        assertTrue(outputStreamCaptor.toString().contains("Math 101"));
    }

    @Test
    void testViewGradesForStudent() {
        Student student = new Student(1, "John Doe", "john@example.com");
        Course course = new Course(1, "Math 101", 5, 500, new ArrayList<>());
        Enrollment enrollment = new Enrollment(1, student, course);
        enrollment.setGrade(85);
        system.setEnrollments(List.of(enrollment));

        Scanner scanner = simulateInput("1\n");
        system.viewGradesForStudent(scanner);

        assertTrue(outputStreamCaptor.toString().contains("Math 101"));
        assertTrue(outputStreamCaptor.toString().contains("85"));
    }

    @Test
void testViewStudentsInCourseInvalidId() {
    // Simulate invalid course ID input
    system.setCourses(List.of(
            new Course(1, "Math 101", 5, 500, new ArrayList<>())
    ));
    Scanner scanner = simulateInput("2\n");
    system.viewStudentsInCourse(scanner);

    assertTrue(outputStreamCaptor.toString().contains("Course not found."));
}

@Test
void testMakePaymentInvalidInput() {
    Student student = new Student(1, "John Doe", "john@example.com");
    Course course = new Course(1, "Math 101", 5, 500, new ArrayList<>());
    Enrollment enrollment = new Enrollment(1, student, course);
    system.setEnrollments(List.of(enrollment));

    // Simulate invalid inputs for Enrollment ID
    Scanner scanner = simulateInput("5\n200\n");
    system.makePayment(scanner);

    assertTrue(outputStreamCaptor.toString().contains("Invalid Enrollment ID or fee already paid."));
}

@Test
void testViewCoursesForStudentInvalidId() {
    // Simulate invalid student ID input
    system.setStudents(List.of(
            new Student(1, "John Doe", "john@example.com")
    ));
    Scanner scanner = simulateInput("2\n");
    system.viewCoursesForStudent(scanner);

    assertTrue(outputStreamCaptor.toString().contains("Student not found."));
}

@Test
void testViewGradesForStudentInvalidId() {
    // Simulate invalid student ID input
    system.setStudents(List.of(
            new Student(1, "John Doe", "john@example.com")
    ));
    Scanner scanner = simulateInput("2\n");
    system.viewGradesForStudent(scanner);

    assertTrue(outputStreamCaptor.toString().contains("Grades for student 2:"));
    assertFalse(outputStreamCaptor.toString().contains("John Doe"));
}
}
