import java.util.*;

class CCRMSystem {
    private Map<String, Student> students;
    private Map<String, Course> courses;
    private Scanner scanner;
    
    public CCRMSystem() {
        students = new HashMap<>();
        courses = new HashMap<>();
        scanner = new Scanner(System.in);
        FileManager.ensureDirectories();
    }
    
    public void displayMainMenu() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("    CAMPUS COURSE & RECORDS MANAGER (CCRM)");
        System.out.println("=".repeat(50));
        System.out.println("1. Student Management");
        System.out.println("2. Course Management");
        System.out.println("3. Grades & Transcripts");
        System.out.println("4. File Utilities");
        System.out.println("5. Reports & Statistics");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
    }
    
    public void studentManagement() {
        while (true) {
            System.out.println("\n--- STUDENT MANAGEMENT ---");
            System.out.println("1. Add Student");
            System.out.println("2. Update Student");
            System.out.println("3. List All Students");
            System.out.println("4. Search Student");
            System.out.println("5. Enroll Student in Course");
            System.out.println("6. Unenroll Student from Course");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter choice: ");
            
            int choice = getIntInput();
            switch (choice) {
                case 1: addStudent(); break;
                case 2: updateStudent(); break;
                case 3: listAllStudents(); break;
                case 4: searchStudent(); break;
                case 5: enrollStudentInCourse(); break;
                case 6: unenrollStudentFromCourse(); break;
                case 0: return;
                default: System.out.println("Invalid choice!");
            }
        }
    }
    
    public void courseManagement() {
        while (true) {
            System.out.println("\n--- COURSE MANAGEMENT ---");
            System.out.println("1. Add Course");
            System.out.println("2. Update Course");
            System.out.println("3. List All Courses");
            System.out.println("4. Search Course");
            System.out.println("5. Assign Instructor");
            System.out.println("6. View Course Enrollment");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter choice: ");
            
            int choice = getIntInput();
            switch (choice) {
                case 1: addCourse(); break;
                case 2: updateCourse(); break;
                case 3: listAllCourses(); break;
                case 4: searchCourse(); break;
                case 5: assignInstructor(); break;
                case 6: viewCourseEnrollment(); break;
                case 0: return;
                default: System.out.println("Invalid choice!");
            }
        }
    }
    
    public void gradesAndTranscripts() {
        while (true) {
            System.out.println("\n--- GRADES & TRANSCRIPTS ---");
            System.out.println("1. Record Grade");
            System.out.println("2. Update Grade");
            System.out.println("3. View Student Grades");
            System.out.println("4. Calculate GPA");
            System.out.println("5. Print Transcript");
            System.out.println("6. Grade Statistics");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter choice: ");
            
            int choice = getIntInput();
            switch (choice) {
                case 1: recordGrade(); break;
                case 2: updateGrade(); break;
                case 3: viewStudentGrades(); break;
                case 4: calculateGPA(); break;
                case 5: printTranscript(); break;
                case 6: gradeStatistics(); break;
                case 0: return;
                default: System.out.println("Invalid choice!");
            }
        }
    }
    
    public void fileUtilities() {
        while (true) {
            System.out.println("\n--- FILE UTILITIES ---");
            System.out.println("1. Export Students to CSV");
            System.out.println("2. Export Courses to CSV");
            System.out.println("3. Create Backup");
            System.out.println("4. Restore from Backup");
            System.out.println("5. List Backup Files");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter choice: ");
            
            int choice = getIntInput();
            switch (choice) {
                case 1: exportStudentsToCSV(); break;
                case 2: exportCoursesToCSV(); break;
                case 3: createBackup(); break;
                case 4: restoreFromBackup(); break;
                case 5: FileManager.listBackupFiles(); break;
                case 0: return;
                default: System.out.println("Invalid choice!");
            }
        }
    }
    
    // Student Management Methods
    private void addStudent() {
        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine();
        
        if (students.containsKey(studentId)) {
            System.out.println("Student with ID " + studentId + " already exists!");
            return;
        }
        
        System.out.print("Enter First Name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter Last Name: ");
        String lastName = scanner.nextLine();
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();
        
        Student student = new Student(studentId, firstName, lastName, email);
        students.put(studentId, student);
        System.out.println("Student added successfully!");
    }
    
    private void updateStudent() {
        System.out.print("Enter Student ID to update: ");
        String studentId = scanner.nextLine();
        
        Student student = students.get(studentId);
        if (student == null) {
            System.out.println("Student not found!");
            return;
        }
        
        System.out.println("Current details: " + student);
        System.out.print("Enter new First Name (or press Enter to keep current): ");
        String firstName = scanner.nextLine();
        if (!firstName.isEmpty()) student.setFirstName(firstName);
        
        System.out.print("Enter new Last Name (or press Enter to keep current): ");
        String lastName = scanner.nextLine();
        if (!lastName.isEmpty()) student.setLastName(lastName);
        
        System.out.print("Enter new Email (or press Enter to keep current): ");
        String email = scanner.nextLine();
        if (!email.isEmpty()) student.setEmail(email);
        
        System.out.println("Student updated successfully!");
    }
    
    private void listAllStudents() {
        if (students.isEmpty()) {
            System.out.println("No students found.");
            return;
        }
        
        System.out.println("\n--- ALL STUDENTS ---");
        for (Student student : students.values()) {
            System.out.println(student);
        }
    }
    
    private void searchStudent() {
        System.out.print("Enter Student ID or Name to search: ");
        String searchTerm = scanner.nextLine().toLowerCase();
        
        System.out.println("\n--- SEARCH RESULTS ---");
        boolean found = false;
        for (Student student : students.values()) {
            if (student.getStudentId().toLowerCase().contains(searchTerm) ||
                student.getFullName().toLowerCase().contains(searchTerm)) {
                System.out.println(student);
                found = true;
            }
        }
        
        if (!found) {
            System.out.println("No students found matching: " + searchTerm);
        }
    }
    
    private void enrollStudentInCourse() {
        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine();
        System.out.print("Enter Course ID: ");
        String courseId = scanner.nextLine();
        
        Student student = students.get(studentId);
        Course course = courses.get(courseId);
        
        if (student == null) {
            System.out.println("Student not found!");
            return;
        }
        if (course == null) {
            System.out.println("Course not found!");
            return;
        }
        
        if (course.isFull()) {
            System.out.println("Course is full!");
            return;
        }
        
        if (student.getEnrolledCourses().contains(courseId)) {
            System.out.println("Student is already enrolled in this course!");
            return;
        }
        
        student.enrollInCourse(courseId);
        course.addStudent(studentId);
        System.out.println("Student enrolled successfully!");
    }
    
    private void unenrollStudentFromCourse() {
        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine();
        System.out.print("Enter Course ID: ");
        String courseId = scanner.nextLine();
        
        Student student = students.get(studentId);
        Course course = courses.get(courseId);
        
        if (student == null) {
            System.out.println("Student not found!");
            return;
        }
        if (course == null) {
            System.out.println("Course not found!");
            return;
        }
        
        student.unenrollFromCourse(courseId);
        course.removeStudent(studentId);
        System.out.println("Student unenrolled successfully!");
    }
    
    // Course Management Methods
    private void addCourse() {
        System.out.print("Enter Course ID: ");
        String courseId = scanner.nextLine();
        
        if (courses.containsKey(courseId)) {
            System.out.println("Course with ID " + courseId + " already exists!");
            return;
        }
        
        System.out.print("Enter Course Name: ");
        String courseName = scanner.nextLine();
        System.out.print("Enter Description: ");
        String description = scanner.nextLine();
        System.out.print("Enter Credits: ");
        int credits = getIntInput();
        System.out.print("Enter Maximum Students: ");
        int maxStudents = getIntInput();
        
        Course course = new Course(courseId, courseName, description, credits, maxStudents);
        courses.put(courseId, course);
        System.out.println("Course added successfully!");
    }
    
    private void updateCourse() {
        System.out.print("Enter Course ID to update: ");
        String courseId = scanner.nextLine();
        
        Course course = courses.get(courseId);
        if (course == null) {
            System.out.println("Course not found!");
            return;
        }
        
        System.out.println("Current details: " + course);
        System.out.print("Enter new Course Name (or press Enter to keep current): ");
        String courseName = scanner.nextLine();
        if (!courseName.isEmpty()) course.setCourseName(courseName);
        
        System.out.print("Enter new Description (or press Enter to keep current): ");
        String description = scanner.nextLine();
        if (!description.isEmpty()) course.setDescription(description);
        
        System.out.print("Enter new Credits (or -1 to keep current): ");
        int credits = getIntInput();
        if (credits != -1) course.setCredits(credits);
        
        System.out.print("Enter new Max Students (or -1 to keep current): ");
        int maxStudents = getIntInput();
        if (maxStudents != -1) course.setMaxStudents(maxStudents);
        
        System.out.println("Course updated successfully!");
    }
    
    private void listAllCourses() {
        if (courses.isEmpty()) {
            System.out.println("No courses found.");
            return;
        }
        
        System.out.println("\n--- ALL COURSES ---");
        for (Course course : courses.values()) {
            System.out.println(course);
        }
    }
    
    private void searchCourse() {
        System.out.print("Enter Course ID or Name to search: ");
        String searchTerm = scanner.nextLine().toLowerCase();
        
        System.out.println("\n--- SEARCH RESULTS ---");
        boolean found = false;
        for (Course course : courses.values()) {
            if (course.getCourseId().toLowerCase().contains(searchTerm) ||
                course.getCourseName().toLowerCase().contains(searchTerm)) {
                System.out.println(course);
                found = true;
            }
        }
        
        if (!found) {
            System.out.println("No courses found matching: " + searchTerm);
        }
    }
    
    private void assignInstructor() {
        System.out.print("Enter Course ID: ");
        String courseId = scanner.nextLine();
        
        Course course = courses.get(courseId);
        if (course == null) {
            System.out.println("Course not found!");
            return;
        }
        
        System.out.print("Enter Instructor Name: ");
        String instructor = scanner.nextLine();
        course.setInstructor(instructor);
        System.out.println("Instructor assigned successfully!");
    }
    
    private void viewCourseEnrollment() {
        System.out.print("Enter Course ID: ");
        String courseId = scanner.nextLine();
        
        Course course = courses.get(courseId);
        if (course == null) {
            System.out.println("Course not found!");
            return;
        }
        
        System.out.println("\n--- COURSE ENROLLMENT ---");
        System.out.println("Course: " + course.getCourseName());
        System.out.println("Enrolled Students (" + course.getEnrolledCount() + "/" + course.getMaxStudents() + "):");
        
        for (String studentId : course.getEnrolledStudents()) {
            Student student = students.get(studentId);
            if (student != null) {
                System.out.println("- " + student.getFullName() + " (" + studentId + ")");
            }
        }
    }
    
    // Grades and Transcripts Methods
    private void recordGrade() {
        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine();
        System.out.print("Enter Course ID: ");
        String courseId = scanner.nextLine();
        
        Student student = students.get(studentId);
        if (student == null) {
            System.out.println("Student not found!");
            return;
        }
        
        if (!student.getEnrolledCourses().contains(courseId)) {
            System.out.println("Student is not enrolled in this course!");
            return;
        }
        
        System.out.print("Enter Grade (0-100): ");
        double grade = getDoubleInput();
        
        if (grade < 0 || grade > 100) {
            System.out.println("Invalid grade! Must be between 0 and 100.");
            return;
        }
        
        student.setGrade(courseId, grade);
        System.out.println("Grade recorded successfully!");
    }
    
    private void updateGrade() {
        recordGrade(); // Same functionality as recording
    }
    
    private void viewStudentGrades() {
        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine();
        
        Student student = students.get(studentId);
        if (student == null) {
            System.out.println("Student not found!");
            return;
        }
        
        System.out.println("\n--- STUDENT GRADES ---");
        System.out.println("Student: " + student.getFullName());
        System.out.println("Grades:");
        
        if (student.getGrades().isEmpty()) {
            System.out.println("No grades recorded.");
            return;
        }
        
        for (Map.Entry<String, Double> entry : student.getGrades().entrySet()) {
            Course course = courses.get(entry.getKey());
            String courseName = course != null ? course.getCourseName() : "Unknown Course";
            System.out.printf("- %s (%s): %.2f%n", courseName, entry.getKey(), entry.getValue());
        }
        System.out.printf("GPA: %.2f%n", student.calculateGPA());
    }
    
    private void calculateGPA() {
        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine();
        
        Student student = students.get(studentId);
        if (student == null) {
            System.out.println("Student not found!");
            return;
        }
        
        double gpa = student.calculateGPA();
        System.out.printf("GPA for %s: %.2f%n", student.getFullName(), gpa);
    }
    
    private void printTranscript() {
        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine();
        
        Student student = students.get(studentId);
        if (student == null) {
            System.out.println("Student not found!");
            return;
        }
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("                    OFFICIAL TRANSCRIPT");
        System.out.println("=".repeat(60));
        System.out.println("Student ID: " + student.getStudentId());
        System.out.println("Name: " + student.getFullName());
        System.out.println("Email: " + student.getEmail());
        System.out.println("Enrollment Date: " + student.getEnrollmentDate());
        System.out.println("-".repeat(60));
        System.out.println("COURSES AND GRADES:");
        
        if (student.getGrades().isEmpty()) {
            System.out.println("No grades recorded.");
        } else {
            for (Map.Entry<String, Double> entry : student.getGrades().entrySet()) {
                Course course = courses.get(entry.getKey());
                if (course != null) {
                    System.out.printf("%-20s %-30s %3d credits Grade: %.2f%n", 
                                    entry.getKey(), course.getCourseName(), 
                                    course.getCredits(), entry.getValue());
                }
            }
        }
        
        System.out.println("-".repeat(60));
        System.out.printf("CUMULATIVE GPA: %.2f%n", student.calculateGPA());
        System.out.println("=".repeat(60));
    }
    
    private void gradeStatistics() {
        if (students.isEmpty()) {
            System.out.println("No students found.");
            return;
        }
        
        double totalGPA = 0.0;
        int studentsWithGrades = 0;
        double highestGPA = 0.0;
        double lowestGPA = 100.0;
        String topStudent = "";
        
        for (Student student : students.values()) {
            double gpa = student.calculateGPA();
            if (gpa > 0) {
                totalGPA += gpa;
                studentsWithGrades++;
                
                if (gpa > highestGPA) {
                    highestGPA = gpa;
                    topStudent = student.getFullName();
                }
                
                if (gpa < lowestGPA) {
                    lowestGPA = gpa;
                }
            }
        }
        
        System.out.println("\n--- GRADE STATISTICS ---");
        if (studentsWithGrades > 0) {
            System.out.printf("Average GPA: %.2f%n", totalGPA / studentsWithGrades);
            System.out.printf("Highest GPA: %.2f (%s)%n", highestGPA, topStudent);
            System.out.printf("Lowest GPA: %.2f%n", lowestGPA);
            System.out.println("Students with grades: " + studentsWithGrades);
        } else {
            System.out.println("No grades recorded yet.");
        }
    }
    
    // File Utilities Methods
    private void exportStudentsToCSV() {
        System.out.print("Enter filename (e.g., students.csv): ");
        String filename = scanner.nextLine();
        FileManager.exportStudentsToCSV(students, filename);
    }
    
    private void exportCoursesToCSV() {
        System.out.print("Enter filename (e.g., courses.csv): ");
        String filename = scanner.nextLine();
        FileManager.exportCoursesToCSV(courses, filename);
    }
    
    private void createBackup() {
        FileManager.createBackup(students, courses);
    }
    
    private void restoreFromBackup() {
        FileManager.listBackupFiles();
        System.out.print("Enter students backup filename: ");
        String studentsFile = scanner.nextLine();
        System.out.print("Enter courses backup filename: ");
        String coursesFile = scanner.nextLine();
        
        Map<String, Student> backupStudents = FileManager.loadStudentsBackup(studentsFile);
        Map<String, Course> backupCourses = FileManager.loadCoursesBackup(coursesFile);
        
        if (!backupStudents.isEmpty()) {
            students = backupStudents;
            System.out.println("Students restored successfully!");
        }
        
        if (!backupCourses.isEmpty()) {
            courses = backupCourses;
            System.out.println("Courses restored successfully!");
        }
    }
    
    // Reports and Statistics
    private void reportsAndStatistics() {
        System.out.println("\n--- REPORTS & STATISTICS ---");
        System.out.println("Total Students: " + students.size());
        System.out.println("Total Courses: " + courses.size());
        
        // Course enrollment statistics
        int totalEnrollments = 0;
        for (Course course : courses.values()) {
            totalEnrollments += course.getEnrolledCount();
        }
        System.out.println("Total Enrollments: " + totalEnrollments);
        
        if (!courses.isEmpty()) {
            double avgEnrollment = (double) totalEnrollments / courses.size();
            System.out.printf("Average Enrollment per Course: %.2f%n", avgEnrollment);
        }
        
        gradeStatistics();
    }
    
    // Utility Methods
    private int getIntInput() {
        try {
            int value = Integer.parseInt(scanner.nextLine());
            return value;
        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Please enter a valid number.");
            return -1;
        }
    }
    
    private double getDoubleInput() {
        try {
            double value = Double.parseDouble(scanner.nextLine());
            return value;
        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Please enter a valid number.");
            return -1;
        }
    }
    
    public void run() {
        System.out.println("Welcome to Campus Course & Records Manager (CCRM)!");
        
        while (true) {
            displayMainMenu();
            int choice = getIntInput();
            
            switch (choice) {
                case 1:
                    studentManagement();
                    break;
                case 2:
                    courseManagement();
                    break;
                case 3:
                    gradesAndTranscripts();
                    break;
                case 4:
                    fileUtilities();
                    break;
                case 5:
                    reportsAndStatistics();
                    break;
                case 0:
                    System.out.println("Thank you for using CCRM. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }
}

