package com.example.studentmanagement;

import java.util.*;
import com.example.studentmanagement.entities.Student;

public class Main {

    public static void main(String[] args) {

        AdminManagementSystem adminManagementSystem;
        StudentManagementSystem studentManagementSystem;
        InstructorManagementSystem instructorManagementSystem;

        UserManager userManager = new UserManager();

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        // Register initial users
        userManager.registerUser("admin", "admin123", "Admin");
        userManager.registerUser("instructor", "inst123", "Instructor");
        userManager.registerUser("student", "stud123", "Student");

        System.out.println("=== Welcome to the Management System ===");

        while (running) {
            // Check if a user is logged in
            if (userManager.getLoggedInUser() == null) {
                System.out.println("\nPlease log in to continue:");
                System.out.print("Username: ");
                String username = scanner.nextLine();                
                System.out.print("Password: ");
                String password = scanner.nextLine();

                if (!userManager.loginUser(username, password)) {
                    System.out.println("Invalid login. Try again.");
                    continue;
                }
            }

            // Display menu based on user role
            String role = userManager.getLoggedInUser().getRole();
            System.out.println("\n=== Main Menu (" + role + ") ===");
            if ("Admin".equals(role)) {
                displayAdminMenu();
            } else if ("Instructor".equals(role)) {
                displayInstructorMenu();
            } else if ("Student".equals(role)) {
                displayStudentMenu();
            }

            // Get user choice
            System.out.print("\nEnter your choice: ");
            int choice = Integer.parseInt(scanner.nextLine());

            // Handle choice based on the role
            switch (role) {
                case "Admin":
                    adminManagementSystem = new AdminManagementSystem();
                    adminManagementSystem.loadData();
                    handleAdminChoice(choice, adminManagementSystem, scanner, userManager);
                    adminManagementSystem.saveData();
                    break;
                case "Instructor":
                    instructorManagementSystem = new InstructorManagementSystem();
                    instructorManagementSystem.loadData();
                    handleInstructorChoice(choice, instructorManagementSystem, scanner);
                    instructorManagementSystem.saveData();
                    break;
                case "Student":
                    studentManagementSystem = new StudentManagementSystem();
                    studentManagementSystem.loadData();
                    handleStudentChoice(choice, studentManagementSystem, scanner);
                    studentManagementSystem.saveData();
                    break;
                default:
                    System.out.println("Invalid role. Logging out...");
                    userManager.logoutUser();
            }

            // Exit system
            if (choice == 0) {
                running = false;
            }
        }

        // Save data before exiting
        System.out.println("Goodbye!");
    }

    // Menu display methods
    private static void displayAdminMenu() {
        System.out.println("1. Add Student");
        System.out.println("2. Add Course");
        System.out.println("3. Add Prerequisite Course");
        System.out.println("4. View All Students");
        System.out.println("5. View All Courses");
        System.out.println("6. View All Enrollments");
        System.out.println("7. Update Student");
        System.out.println("8. Delete Student");
        System.out.println("9. Generate Outstanding Fee Report");
        System.out.println("10. Export Data as Text");
        System.out.println("11. Create Backup");
        System.out.println("12. Filter Courses By Duration");
        System.out.println("13. Search for a course");
        System.out.println("0. Exit");
    }

    private static void displayInstructorMenu() {
        System.out.println("1. View Students in a course");
        System.out.println("2. Award Achievements");
        System.out.println("3. View Achievements");
        System.out.println("4. Search Student");
        System.out.println("5. Add grade for a student");
        System.out.println("6. Calculate Average grade for a course");
        System.out.println("0. Exit");
    }

    private static void displayStudentMenu() {
        System.out.println("1. View Notifications");
        System.out.println("2. View Achievements");
        System.out.println("3. Make Payment");
        System.out.println("4. View Outstanding Fees");
        System.out.println("5. Enroll in course");
        System.out.println("6. View Enrolled Courses");
        System.out.println("7. View Grades");
        System.out.println("0. Exit");
    }

    // Handle Admin Choices
    private static void handleAdminChoice(int choice, AdminManagementSystem managementSystem, Scanner scanner,
            UserManager userManager) {
        switch (choice) {
            case 1:
                managementSystem.addStudent(scanner);
                break;
            case 2:
                managementSystem.addCourse(scanner);
                break;
            case 3:
                managementSystem.addPrerequisite(scanner);
                break;
            case 4:
                managementSystem.viewAllStudents();
                break;
            case 5:
                managementSystem.viewAllCourses();
                break;
            case 6:
                managementSystem.viewEnrollments();
                break;
            case 7:
                managementSystem.updateStudent(scanner);
                break;
            case 8:
                managementSystem.deleteStudent(scanner);
                break;
            case 9:
                managementSystem.generateOutstandingReport();
                break;
            case 10:
                System.out.print("Enter filename for export: ");
                String filename = scanner.nextLine();
                managementSystem.exportDataAsText(filename);
                break;
            case 11:
                managementSystem.createBackup();
                break;
            case 12:
                managementSystem.filterCoursesByDuration(scanner);
                break;
            case 13:
                managementSystem.searchCourse(scanner);
                break;
            case 0:
                System.out.println("Logging out...");
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    // Handle Instructor Choices
    private static void handleInstructorChoice(int choice, InstructorManagementSystem managementSystem, Scanner scanner) {
        switch (choice) {
            case 1:
                managementSystem.viewStudentsInCourse(scanner);
                break;
            case 2:
                System.out.print("Enter Student ID: ");
                int studentId = scanner.nextInt();
                scanner.nextLine();
                System.out.print("Enter Achievement Title: ");
                String title = scanner.nextLine();
                Student student = managementSystem.getStudents().stream()
                .filter(s-> s.getId() == studentId)
                .findFirst()
                .orElse(null);
                if (student != null) {
                    managementSystem.awardAchievement(student, title);
                } else {
                    System.out.println("Student not found.");
                }
                break;
            case 3:
                managementSystem.listAchievements();
                break;
            case 4:
                managementSystem.searchStudent(scanner);
                break;
            case 5:
                managementSystem.addGrade(scanner);
                break;
            case 6:
                managementSystem.calculateAverageGradeForCourse(scanner);
                break;
            case 0:
                System.out.println("Logging out...");
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    // Handle Student Choices
    private static void handleStudentChoice(int choice, StudentManagementSystem managementSystem, Scanner scanner) {
        switch (choice) {
            case 1:
                managementSystem.viewNotifications();
                break;
            case 2:
                managementSystem.listAchievements();
                break;
            case 3:
                managementSystem.makePayment(scanner);
                break;
            case 4:
                managementSystem.generateOutstandingReport(scanner);
                break;
            case 5:
                managementSystem.enrollStudent(scanner);
                break;
            case 6:
                managementSystem.viewCoursesForStudent(scanner);
                break;
            case 7:
                managementSystem.viewGradesForStudent(scanner);
                break;
            case 0:
                System.out.println("Logging out...");
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }
}