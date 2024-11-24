package com.example.studentmanagement;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;
import com.example.studentmanagement.entities.Achievement;
import com.example.studentmanagement.entities.Course;
import com.example.studentmanagement.entities.Enrollment;
import com.example.studentmanagement.entities.Notification;
import com.example.studentmanagement.entities.Payment;
import com.example.studentmanagement.entities.Student;

public class AdminManagementSystem {
    private List<Student> students;
    private List<Course> courses;
    private List<Enrollment> enrollments;
    private List<Payment> payments;
    private List<Achievement> achievements;
    private List<Notification> notifications;   
    
    public AdminManagementSystem(){
        students = new ArrayList<>();        
        courses = new ArrayList<>();
        enrollments = new ArrayList<>();
        payments = new ArrayList<>();
        achievements = new ArrayList<>();
        notifications = new ArrayList<>();
    }

    public List<Student> getStudents() {
        return students;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public List<Enrollment> getEnrollments() {
        return enrollments;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    // Method to load data from files
    public void loadData() {
        students = FileStorage.loadFromFile("students.dat");
        courses = FileStorage.loadFromFile("courses.dat");
        enrollments = FileStorage.loadFromFile("enrollments.dat");
        payments = FileStorage.loadFromFile("payments.dat");
        notifications = FileStorage.loadFromFile("notifications.dat");
        achievements = FileStorage.loadFromFile("achievements.dat");
        System.out.println("Data loaded successfully.");
    }

    // Method to save data to files
    public void saveData() {
        FileStorage.saveToFile("students.dat", students);
        FileStorage.saveToFile("courses.dat", courses);
        FileStorage.saveToFile("enrollments.dat", enrollments);
        FileStorage.saveToFile("payments.dat", payments);
        FileStorage.saveToFile("notifications.dat", notifications);
        FileStorage.saveToFile("achievements.dat", achievements);
        System.out.println("Data saved successfully.");
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
    }

    // Admin Features
    // Add a prerequisite for a course
    public void addPrerequisite(Scanner scanner) {
        System.out.print("Enter Course ID: ");
        int courseId = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter Prerequisite Course ID: ");
        int prerequisiteId = scanner.nextInt();
        scanner.nextLine();

        Course course = findCourseById(courseId);
        if(course == null){
            System.out.println("Course not found!");
            return;
        }
        course.addPrerequisites(prerequisiteId);
        System.out.println("Prerequisite added successfully!");
    }

    // Add a student
    public void addStudent(Scanner scanner) {
        System.out.print("Enter Student ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter Student Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Student Email: ");
        String email = scanner.nextLine();

        if (!isValidEmail(email)) {
            System.out.println("Invalid email format. Student not added.");
            return;
        }

        students.add(new Student(id, name, email));
        notifications.add(new Notification("Welcome " + name + " to the system!", findStudentById(id)));
        System.out.println("Student added successfully!");
    }

    // Add a course
    public void addCourse(Scanner scanner) {
        System.out.print("Enter Course ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter Course Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Course Duration (in weeks): ");
        int duration = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter Fee Amount: ");
        double fee = scanner.nextDouble();
        scanner.nextLine();

        courses.add(new Course(id, name, duration, fee, new ArrayList<Integer>()));
        System.out.println("Course added successfully!");
    }

    // View all students
    public void viewAllStudents() {
        if (students.isEmpty()) {
            System.out.println("No students available.");
        } else {
            System.out.println("\n=== List of Students ===");
            for (Student student : students) {
                System.out.println(student);
            }
        }
    }

    // View all courses
    public void viewAllCourses() {
        if (courses.isEmpty()) {
            System.out.println("No courses available.");
        } else {
            System.out.println("\n=== List of Courses ===");
            for (Course course : courses) {
                System.out.println(course);
            }
        }
    }

    // View all enrollments
    public void viewEnrollments() {
        if (enrollments.isEmpty()) {
            System.out.println("No enrollments available.");
        } else {
            System.out.println("\n=== List of Enrollments ===");
            for (Enrollment enrollment : enrollments) {
                System.out.println(enrollment);
            }
        }
    }

    // Generate outstanding fee report
    public void generateOutstandingReport() {
        System.out.println("=== Outstanding Fee Report ===");
        enrollments.stream()
                .filter(e -> !e.isFeePaid())
                .forEach(e -> {
                    System.out.println("Student: " + e.getStudent().getName() +
                            ", Course: " + e.getCourse().getName() +
                            ", Outstanding: Rs." + e.getBalanceAmt());
                });
    }

    // Update student details
    public void updateStudent(Scanner scanner) {
        System.out.print("Enter Student ID to update: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        Student student = findStudentById(id);
        if (student != null) {
            System.out.print("Enter new name (leave blank to keep current): ");
            String newName = scanner.nextLine();
            System.out.print("Enter new email (leave blank to keep current): ");
            String newEmail = scanner.nextLine();

            if(!newEmail.isEmpty() && isValidEmail(newEmail)){
                if (!newName.isEmpty())
                    student.setName(newName);
                if (!newEmail.isEmpty())
                    student.setEmail(newEmail);

                System.out.println("Student updated successfully!");
                notifications.add(new Notification("Student " + newName + ", your details have been updated successfully",
                        findStudentById(id)));
            } else {
                System.out.println("Invalid email, student not updated.");
            }

            
        } else {
            System.out.println("Student not found.");
        }
    }

    // Remove a student
    public void deleteStudent(Scanner scanner) {
        System.out.print("Enter Student ID to delete: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        Student student = findStudentById(id);

        if (student != null) {
            students.remove(student);
            enrollments.removeIf(e -> e.getStudent().getId() == id);
            notifications.removeIf(n -> n.getStudentId() == id);
            System.out.println("Student deleted successfully.");
            saveData();
        } else {
            System.out.println("Student not found.");
        }
    }

    // Filter Course by duration
    public void filterCoursesByDuration(Scanner scanner) {
        System.out.print("Enter minimum duration (in weeks): ");
        int minDuration = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Courses with duration >= " + minDuration + " weeks:");
        for (Course course : courses) {
            if (course.getDuration() >= minDuration) {
                System.out.println(course);
            }
        }
    }

    // Export all data as text
    public void exportDataAsText(String filename) {
        try (PrintWriter writer = new PrintWriter(new File(filename))) {
            writer.println("=== Students ===");
            for (Student student : students)
                writer.println(student);
            writer.println("\n=== Courses ===");
            for (Course course : courses)
                writer.println(course);
            writer.println("\n=== Enrollments ===");
            for (Enrollment enrollment : enrollments)
                writer.println(enrollment);
            writer.println("\n=== Payments ===");
            for (Payment payment : payments)
                writer.println(payment);
            System.out.println("Data exported to " + filename + " successfully!");
        } catch (IOException e) {
            System.out.println("Error exporting data: " + e.getMessage());
        }
    }

    // Search for a student by ID
    public Student searchStudent(Scanner scanner) {
        System.out.print("Enter Student ID to search: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        Student student = findStudentById(id);

        if (student != null) {
            System.out.println(student);
            return(student);            
        } else { 
            System.out.println("Student not found.");           
            return null;
        }
    }

    // Search for a course by ID
    public Course searchCourse(Scanner scanner) {
        System.out.print("Enter Course ID to search: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        Course course = findCourseById(id);

        if (course != null) {
            System.out.println(course);
            return course;
        } else {
            System.out.println("Course not found.");
            return null;
        }
    }

    // Find a student by ID
    private Student findStudentById(int id) {
        for (Student student : students) {
            if (student.getId() == id) {
                return student;
            }
        }
        return null;
    }

    // Find a course by ID
    private Course findCourseById(int id) {
        for (Course course : courses) {
            if (course.getId() == id) {
                return course;
            }
        }
        return null;
    }

    public void createBackup() {
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        saveData();
        System.out.println("Backup created with timestamp: " + timestamp);
    }
    
}