package com.example.studentmanagement;

import java.util.*;
import com.example.studentmanagement.entities.Achievement;
import com.example.studentmanagement.entities.Course;
import com.example.studentmanagement.entities.Enrollment;
import com.example.studentmanagement.entities.Notification;
import com.example.studentmanagement.entities.Payment;
import com.example.studentmanagement.entities.Student;

public class InstructorManagementSystem {

    private List<Student> students;
    private List<Course> courses;
    private List<Enrollment> enrollments;
    private List<Payment> payments;
    private List<Achievement> achievements;
    private List<Notification> notifications;

    public List<Student> getStudents() {
        return students;
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

    // Add grade to a student
    public void addGrade(Scanner scanner) {
        System.out.print("Enter Student ID: ");
        int studentId = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter Course ID: ");
        int courseId = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter Grade: ");
        double grade = scanner.nextDouble();
        scanner.nextLine();

        for (Enrollment enrollment : enrollments) {
            if (enrollment.getStudent().getId() == studentId && enrollment.getCourse().getId() == courseId) {
                enrollment.setGrade(grade);
                System.out.println("Grade added successfully!");
                notifications.add(new Notification(
                        "Student, your grade has been added for course " + enrollment.getCourse().getName(),
                        findStudentById(studentId)));
                return;
            }
        }

        System.out.println("Enrollment not found. Cannot add grade.");
    }

    // Get average grade of course
    public void calculateAverageGradeForCourse(Scanner scanner) {
        System.out.print("Enter Course ID: ");
        int courseId = scanner.nextInt();
        scanner.nextLine();

        double totalGrade = 0;
        int count = 0;

        for (Enrollment enrollment : enrollments) {
            if (enrollment.getCourse().getId() == courseId && enrollment.getGrade() != 0) {
                totalGrade = totalGrade + enrollment.getGrade();
                count++;
            }
        }

        if (count > 0) {
            Course course = findCourseById(courseId);
            course.setAvgGrade(totalGrade / count);
            System.out.println("Average grade for course: " + course.getAvgGrade());
        } else {
            System.out.println("No grades available for this course.");
        }
    }

    // Award achievement to student
    public void awardAchievement(Student student, String title) {
        achievements.add(new Achievement(student, title));
        notifications.add(new Notification("You have been awarded: " + title, student));
        System.out.println("Achievement awarded to " + student.getName());
        saveData();
    }

    // View students enrolled in a course
    public void viewStudentsInCourse(Scanner scanner) {
        System.out.print("Enter Course ID: ");
        int courseId = scanner.nextInt();
        scanner.nextLine();

        Course course = findCourseById(courseId);
        if (course != null) {
            System.out.println("Students enrolled in course " + course.getName() + ":");
            for (Enrollment enrollment : enrollments) {
                if (enrollment.getCourse() == course) {
                    System.out.println(enrollment.getStudent());
                }
            }
        } else {
            System.out.println("Course not found.");
        }
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

    // Sort students by name
    public void sortStudentsByName() {
        students.sort((s1, s2) -> s1.getName().compareToIgnoreCase(s2.getName()));
        System.out.println("Students sorted alphabetically:");
        viewAllStudents();
    }

    // List all achievements of students
    public void listAchievements() {
        if (achievements.isEmpty()) {
            System.out.println("No achievements found.");
        } else {
            achievements.forEach(System.out::println);
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
}