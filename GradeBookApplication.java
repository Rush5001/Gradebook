import java.util.InputMismatchException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

// Main class for running the GradeBook application
public class GradeBookApplication {
    private static Scanner scanner = new Scanner(System.in);
    private static ArrayList<Course> courses = new ArrayList<>();

    public static void main(String[] args) {
        loadInitialData();  // Load initial data if needed
        mainMenu();
    }

    // Main menu to navigate between professor and student roles
    public static void mainMenu() {
        int choice;
        do {
            System.out.println("Welcome to the GradeBook Application");
            System.out.println("1. Professor");
            System.out.println("2. Student");
            System.out.println("3. Exit");
            System.out.print("Select your role: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    professorMenu();
                    break;
                case 2:
                    studentMenu();
                    break;
                case 3:
                    System.out.println("Exiting the application. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } while (choice != 3);
    }

    // Professor menu
    public static void professorMenu() {
        int choice;
        do {
            System.out.println(" --- Professor Menu ---");
                    System.out.println("1. Create Course");
            System.out.println("2. View Courses");
            System.out.println("3. Manage Course");
            System.out.println("4. Return to Main Menu");
            System.out.print("Select an option: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    createCourse();
                    break;
                case 2:
                    viewCourses();
                    break;
                case 3:
                    manageCourse();
                    break;
                case 4:
                    System.out.println("Returning to main menu...");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } while (choice != 4);
    }

    // Student menu
    public static void studentMenu() {
        System.out.println(" --- Student Menu ---");
                System.out.println("Enter your student ID to view your grades: ");
        int studentId = scanner.nextInt();
        viewStudentGrades(studentId);
    }

    // Method to create a new course
    public static void createCourse() {
        System.out.println("Enter course name: ");
        scanner.nextLine();  // Consume the newline
        String courseName = scanner.nextLine();
        courses.add(new Course(courseName));
        System.out.println("Course '" + courseName + "' created successfully!");
    }

    // Method to view all courses
    public static void viewCourses() {
        if (courses.isEmpty()) {
            System.out.println("No courses available.");
            return;
        }
        System.out.println(" --- List of Courses ---");
        for (int i = 0; i < courses.size(); i++) {
            System.out.println((i + 1) + ". " + courses.get(i).getName());
        }
    }

    // Method to manage a specific course
    public static void manageCourse() {
        viewCourses();
        System.out.println("Select a course to manage: ");

        // Input validation for course selection
        int courseIndex = -1;
        boolean validInput = false;

        while (!validInput) {
            try {
                if (scanner.hasNextInt()) {
                    courseIndex = scanner.nextInt() - 1;
                    if (courseIndex >= 0 && courseIndex < courses.size()) {
                        validInput = true;  // Valid input found, exit loop
                    } else {
                        System.out.println("Invalid course selection. Please enter a valid course number.");
                    }
                } else {
                    System.out.println("Please enter a number corresponding to a course.");
                    scanner.next();  // Consume the invalid input
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();  // Clear the scanner buffer
            }
        }

        // Manage the selected course if valid
        courses.get(courseIndex).courseMenu();
    }

    // View student grades by student ID
    public static void viewStudentGrades(int studentId) {
        boolean found = false;
        for (Course course : courses) {
            Student student = course.findStudentById(studentId);
            if (student != null) {
                System.out.println("Grades for " + student.getName() + " in " + course.getName() + ":");
                course.displayStudentGrades(student);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No grades found for the given student ID.");
        }
    }

    // Load initial data (optional)
    public static void loadInitialData() {
        // Placeholder for loading data from a file or database
    }
}

// Course class
class Course {
    private String name;
    private ArrayList<Student> students;
    private ArrayList<Assessment> assessments;
    private Scanner scanner = new Scanner(System.in);

    public Course(String name) {
        this.name = name;
        this.students = new ArrayList<>();
        this.assessments = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    // Menu for managing the course
    public void courseMenu() {
        int choice;
        do {
            System.out.println(" --- Managing Course: " + name + " ---");
            System.out.println("1. Add Student");
            System.out.println("2. Add Assessment");
            System.out.println("3. View Student Grades");
            System.out.println("4. Return to Professor Menu");
            System.out.print("Select an option: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    addStudent();
                    break;
                case 2:
                    addAssessment();
                    break;
                case 3:
                    viewStudentGrades();
                    break;
                case 4:
                    System.out.println("Returning to professor menu...");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } while (choice != 4);
    }

    // Add a new student to the course
    public void addStudent() {
        System.out.print("Enter student name: ");
        scanner.nextLine();  // Consume the newline
        String studentName = scanner.nextLine();
        students.add(new Student(studentName));
        System.out.println("Student '" + studentName + "' added successfully!");
    }

    // Add a new assessment to the course
    public void addAssessment() {
        System.out.print("Enter assessment name: ");
        scanner.nextLine();  // Consume the newline
        String assessmentName = scanner.nextLine();
        assessments.add(new Assessment(assessmentName));
        System.out.println("Assessment '" + assessmentName + "' added successfully!");
    }

    // View grades of all students
    public void viewStudentGrades() {
        if (students.isEmpty()) {
            System.out.println("No students enrolled in this course.");
            return;
        }
        for (Student student : students) {
            System.out.println("Grades for " + student.getName() + ":");
            displayStudentGrades(student);
        }
    }

    // Display grades of a specific student
    public void displayStudentGrades(Student student) {
        for (Assessment assessment : assessments) {
            System.out.println(assessment.getName() + ": " + student.getGrade(assessment));
        }
    }

    // Find a student by their ID
    public Student findStudentById(int id) {
        for (Student student : students) {
            if (student.getId() == id) {
                return student;
            }
        }
        return null;
    }
}

// Student class
class Student {
    private static int idCounter = 1;
    private int id;
    private String name;
    private HashMap<Assessment, Double> grades;

    public Student(String name) {
        this.id = idCounter++;
        this.name = name;
        this.grades = new HashMap<>();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    // Set the grade for a specific assessment
    public void setGrade(Assessment assessment, double grade) {
        grades.put(assessment, grade);
    }

    // Get the grade for a specific assessment
    public double getGrade(Assessment assessment) {
        return grades.getOrDefault(assessment, 0.0);
    }

    // Display student information
    @Override
    public String toString() {
        return "Student ID: " + id + ", Name: " + name;
    }
}

// Assessment class
class Assessment {
    private String name;

    public Assessment(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    // Display assessment information
    @Override
    public String toString() {
        return "Assessment: " + name;
    }
}
