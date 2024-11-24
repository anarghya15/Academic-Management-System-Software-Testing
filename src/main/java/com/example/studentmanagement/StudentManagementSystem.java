package com.example.studentmanagement;

import java.util.*;
import java.util.stream.Collectors;

import com.example.studentmanagement.entities.Achievement;
import com.example.studentmanagement.entities.Course;
import com.example.studentmanagement.entities.Enrollment;
import com.example.studentmanagement.entities.Notification;
import com.example.studentmanagement.entities.Payment;
import com.example.studentmanagement.entities.Student;

public class StudentManagementSystem {

    private List<Student> students;
    private List<Course> courses;
    private List<Enrollment> enrollments;
    private int maxCourseCapacity = 30;
    private List<Payment> payments;
    private List<Achievement> achievements;
    private List<Notification> notifications;

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

    public List<Student> getStudents() {
        return students;
    }

    public List<Course> getCourses() {
        return courses;
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

    // View all the notifications
    public void viewNotifications() {
        notifications.forEach(System.out::println);
    }

    // List all achievements of students
    public void listAchievements() {
        if (achievements.isEmpty()) {
            System.out.println("No achievements found.");
        } else {
            achievements.forEach(System.out::println);
        }
    }

    // Make payment for a course
    public void makePayment(Scanner scanner) {
        System.out.print("Enter Enrollment ID: ");
        int enrollmentId = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Enter amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        Enrollment enrollment = enrollments.stream()
                .filter(e -> e.getId() == enrollmentId)
                .findFirst()
                .orElse(null);

        if (enrollment != null && !enrollment.isFeePaid()) {
            payments.add(new Payment(enrollment, amount));
            enrollment.setBalanceAmt(enrollment.getBalanceAmt() - amount);
            if(enrollment.getBalanceAmt() <= 0){
                enrollment.setBalanceAmt(0);
                enrollment.setFeePaid(true);
            }            
            System.out.println("Payment made successfully for " + enrollment.getCourse().getName());
            notifications.add(new Notification("You have made a payment of " + amount + " for course " + enrollment.getCourse().getName(), enrollment.getStudent()));
            saveData();
        } else {
            System.out.println("Invalid Enrollment ID or fee already paid.");
        }
    }

    // Enroll to a course
    public void enrollStudent(Scanner scanner) {
        System.out.print("Enter Student ID: ");
        int studentId = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter Course ID: ");
        int courseId = scanner.nextInt();
        scanner.nextLine();

        Student student = findStudentById(studentId);
        Course course = findCourseById(courseId);

        if (student != null && course != null) {
            if (!hasCompletedPrerequisites(studentId, courseId)) {
                System.out.println("Prerequisites not met. Enrollment failed.");
                return;
            }

            else {
                long count = enrollments.stream().filter(e -> e.getCourse().getId() == courseId).count();
                if (count >= maxCourseCapacity) {
                    System.out.println("Course is full. Enrollment failed.");
                    return;
                }

                enrollments.add(new Enrollment(enrollments.size() + 1, student, course));
                notifications.add(
                        new Notification("You have been enrolled in " + course.getName(), findStudentById(studentId)));
                System.out.println("Enrollment successful!");
            }

        } else {
            System.out.println("Error: Invalid student or course ID.");
        }
    }

    private boolean hasCompletedPrerequisites(int studentId, int courseId) {
        Course course = findCourseById(courseId);
        Student student = findStudentById(studentId);
    
        if (course == null || student == null) {
            System.out.println("Invalid Student ID or Course ID.");
            return false;
        }
    
        List<Integer> prerequisites = course.getPrerequisites();
        if (prerequisites == null || prerequisites.isEmpty()) {
            return true; // No prerequisites
        }
    
        // Find courses completed by the student with a passing grade
        List<Integer> completedCourses = enrollments.stream()
            .filter(e -> e.getStudent().getId() == studentId && e.getGrade() >= 50.0)
            .map(e -> e.getCourse().getId())
            .collect(Collectors.toList());
    
        // Check if all prerequisites are in the completed courses
        for (int prerequisite : prerequisites) {
            if (!completedCourses.contains(prerequisite)) {
                return false; // Missing a prerequisite
            }
        }
    
        return true; // All prerequisites met
    }

    // View courses taken by a student
    public void viewCoursesForStudent(Scanner scanner) {
        System.out.print("Enter Student ID: ");
        int studentId = scanner.nextInt();
        scanner.nextLine();

        Student student = findStudentById(studentId);
        if (student != null) {
            System.out.println("Courses taken by student " + student.getName() + ":");
            for (Enrollment enrollment : enrollments) {
                if (enrollment.getStudent().getId() == studentId) {
                    System.out.println(enrollment.getCourse());
                }
            }
        } else {
            System.out.println("Student not found.");
        }
    }

    // View grades of a student
    public void viewGradesForStudent(Scanner scanner) {
        System.out.print("Enter Student ID: ");
        int studentId = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Grades for student " + studentId + ":");
        for (Enrollment enrollment : enrollments) {
            if (enrollment.getStudent().getId() == studentId) {
                System.out.println("Course: " + enrollment.getCourse().getName() + ", Grade: " + enrollment.getGrade());
            }
        }
    }

    // Generate outstanding fee report
    public void generateOutstandingReport(Scanner scanner) {
        System.out.println("Enter Student ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        System.out.println("=== Outstanding Fee Report ===");
        enrollments.stream()
                .filter(e -> !e.isFeePaid() && e.getStudent().getId() == id)
                .forEach(e -> {
                    System.out.println("Student: " + e.getStudent().getName() +
                            ", Course: " + e.getCourse().getName() +
                            ", Outstanding: Rs." + e.getBalanceAmt());
                });
    }
}