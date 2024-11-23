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
        ManagementSystem system = new ManagementSystem();
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
