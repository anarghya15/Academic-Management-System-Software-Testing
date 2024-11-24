# Student Management System

## Overview

* This repository contains the implementation of a Academic Management System along with its test cases. It demonstrates features like user role-based operations, student-course management, notifications, and more.
* Here we try out Mutation Testing on the Unit Tests that we wrote for the Student Management source code.
* Concepts learned - Unit Testing, Mutation Testing
* Tools learned - JUnit, PIT

## Index

* The source code used for project can be found in src/main folder
* Testing strategy used for the project: Mutation Testing
* Unit test code can be found in the src/test folder
* The mutation testing report can be found in target/pit-reports folder

## Results

![](./screenshots/image1.png)

## Operators Used

![](./screenshots/image5.png)

## Errors Found via Mutation Testing

![](./screenshots/error.png)

![](./screenshots/causeoferror.png)

## Steps To Run

### Project management

* The project is managed with Maven

### Unit testing

* The code was unit tested using JUnit

* To run the unit tests, run the following command:

  ```mvn test```

### Mutation testing

* Mutation testing was applied on the project using PIT Mutation Testing tool

* To run the mutation tests, run the following command:

  ```mvn test-compile org.pitest:pitest-maven:mutationCoverage```

* This will generate an HTML report of mutation testing inside the target/pit-reports folder

### Driver code

* Main.java contains the Driver code of the Student Management System.

* To run the Main.java file, run the following commands:

  ```mvn package```

  ```java -cp ./target/project-1.0-SNAPSHOT.jar studentmanagement.Main```

## Code Description

### AdminManagementSystem.java

This class contains all the functionalities that admin can perform in a system.

Public Methods:
* **addPrerequisite**: Adds a prerequisite course to an existing course.
* **addStudent**: Registers a new student in the system with ID, name, and email.
* **addCourse**: Adds a new course with details like ID, name, duration, and fee.
* **viewAllStudents**: Displays a list of all registered students.
* **viewAllCourses**: Displays a list of all available courses.
* **viewEnrollments**: Shows details of all course enrollments.
* **generateOutstandingReport**: Generates a report of students with outstanding fees.
* **updateStudent**: Updates the details of an existing student.
* **deleteStudent**: Deletes a student and their associated records from the system.
* **filterCoursesByDuration**: Filters and displays courses with a minimum duration.
* **exportDataAsText**: Exports all data (students, courses, enrollments, payments) to a text file.
* **searchStudent**: Searches for a student by their ID and displays their details.
* **searchCourse**: Searches for a course by its ID and displays its details.
* **createBackup**: Creates a backup of the current data with a timestamp.

Private Methods:
* **findStudentById**: Retrieves a student object by their ID.
* **findCourseById**: Retrieves a course object by its ID.

### InstructorManagementSystem.java

This class contains all the functionalities that instructor can perform in a system.

Public Methods:
* **addGrade**: Adds a grade for a student in a specific course.
* **calculateAverageGradeForCourse**: Calculates and displays the average grade of a course.
* **awardAchievement**: Awards an achievement to a student and sends a notification.
* **viewStudentsInCourse**: Lists all students enrolled in a specific course.
* **viewAllStudents**: Displays a list of all students.
* **sortStudentsByName**: Sorts and displays students alphabetically by name.
* **listAchievements**: Lists all achievements awarded to students.
* **searchStudent**: Searches for a student by their ID and displays their details.

Private Methods:
* **findStudentById**: Retrieves a student object by their ID.
* **findCourseById**: Retrieves a course object by its ID.

### StudentManagementSystem.java

This class contains all the functionalities that student can perform in a system.






