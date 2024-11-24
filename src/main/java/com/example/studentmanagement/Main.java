package com.example.studentmanagement;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {

        // Initialize systems and load data
        ManagementSystem managementSystem = new ManagementSystem();
        UserManager userManager = new UserManager();
        // Load data from files
        managementSystem.loadData();

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
                    handleAdminChoice(choice, managementSystem, scanner, userManager);
                    break;
                case "Instructor":
                    handleInstructorChoice(choice, managementSystem, scanner);
                    break;
                case "Student":
                    handleStudentChoice(choice, managementSystem, scanner);
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
        managementSystem.saveData();
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
    private static void handleAdminChoice(int choice, ManagementSystem managementSystem, Scanner scanner,
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
    private static void handleInstructorChoice(int choice, ManagementSystem managementSystem, Scanner scanner) {
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
    private static void handleStudentChoice(int choice, ManagementSystem managementSystem, Scanner scanner) {
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
                managementSystem.generateOutstandingReport();
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

class FileStorage {

    // Generic method to save data to a file
    public static <T> void saveToFile(String filename, List<T> data) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(data);
        } catch (IOException e) {
            System.out.println("Error saving data to file: " + e.getMessage());
        }
    }

    // Generic method to load data from a file
    @SuppressWarnings("unchecked")
    public static <T> List<T> loadFromFile(String filename) {
        File file = new File(filename);
        if (!file.exists()) {
            System.out.println("File not found: " + filename + ". Starting with empty data.");
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<T>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading data from file: " + filename + ". Starting with empty data.");
        }
        return new ArrayList<>();
    }

}

// Management System class that handles all operations
class ManagementSystem {
    private List<Student> students;
    private List<Course> courses;
    private List<Enrollment> enrollments;
    private int maxCourseCapacity = 30;
    private List<Payment> payments;
    private List<Achievement> achievements;
    private List<Notification> notifications;

    public List<Student> getStudents() {
        return students;
    }    

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public List<Enrollment> getEnrollments() {
        return enrollments;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public List<Achievement> getAchievements() {
        return achievements;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }
    
    public void setEnrollments(List<Enrollment> enrollments) {
        this.enrollments = enrollments;
    }

    // Constructor
    public ManagementSystem() {
        students = new ArrayList<>();
        courses = new ArrayList<>();
        enrollments = new ArrayList<>();
        payments = new ArrayList<>();
        achievements = new ArrayList<>();
        notifications = new ArrayList<>();
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

    // Instructor Features
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

    public void createBackup() {
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        saveData();
        System.out.println("Backup created with timestamp: " + timestamp);
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

    // Sort students by name
    public void sortStudentsByName() {
        students.sort((s1, s2) -> s1.getName().compareToIgnoreCase(s2.getName()));
        System.out.println("Students sorted alphabetically:");
        viewAllStudents();
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

    // Student
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
}