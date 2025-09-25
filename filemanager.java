import java.io.*;
import java.util.*;

class FileManager {
    private static final String BACKUP_DIR = "backups/";
    private static final String DATA_DIR = "data/";
    
    public static void ensureDirectories() {
        new File(BACKUP_DIR).mkdirs();
        new File(DATA_DIR).mkdirs();
    }
    
    public static void exportStudentsToCSV(Map<String, Student> students, String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(DATA_DIR + filename))) {
            writer.println("StudentID,FirstName,LastName,Email,EnrollmentDate,GPA");
            for (Student student : students.values()) {
                writer.printf("%s,%s,%s,%s,%s,%.2f%n",
                            student.getStudentId(),
                            student.getFirstName(),
                            student.getLastName(),
                            student.getEmail(),
                            student.getEnrollmentDate(),
                            student.calculateGPA());
            }
            System.out.println("Students exported to " + filename + " successfully!");
        } catch (IOException e) {
            System.out.println("Error exporting students: " + e.getMessage());
        }
    }
    
    public static void exportCoursesToCSV(Map<String, Course> courses, String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(DATA_DIR + filename))) {
            writer.println("CourseID,CourseName,Description,Credits,Instructor,MaxStudents,EnrolledCount");
            for (Course course : courses.values()) {
                writer.printf("%s,%s,\"%s\",%d,%s,%d,%d%n",
                            course.getCourseId(),
                            course.getCourseName(),
                            course.getDescription(),
                            course.getCredits(),
                            course.getInstructor(),
                            course.getMaxStudents(),
                            course.getEnrolledCount());
            }
            System.out.println("Courses exported to " + filename + " successfully!");
        } catch (IOException e) {
            System.out.println("Error exporting courses: " + e.getMessage());
        }
    }
    
    public static void createBackup(Map<String, Student> students, Map<String, Course> courses) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        try {
            // Backup students
            ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(BACKUP_DIR + "students_" + timestamp + ".dat"));
            oos.writeObject(students);
            oos.close();
            
            // Backup courses
            oos = new ObjectOutputStream(
                new FileOutputStream(BACKUP_DIR + "courses_" + timestamp + ".dat"));
            oos.writeObject(courses);
            oos.close();
            
            System.out.println("Backup created successfully with timestamp: " + timestamp);
        } catch (IOException e) {
            System.out.println("Error creating backup: " + e.getMessage());
        }
    }
    
    @SuppressWarnings("unchecked")
    public static Map<String, Student> loadStudentsBackup(String filename) {
        try {
            ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(BACKUP_DIR + filename));
            Map<String, Student> students = (Map<String, Student>) ois.readObject();
            ois.close();
            return students;
        } catch (Exception e) {
            System.out.println("Error loading students backup: " + e.getMessage());
            return new HashMap<>();
        }
    }
    
    @SuppressWarnings("unchecked")
    public static Map<String, Course> loadCoursesBackup(String filename) {
        try {
            ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(BACKUP_DIR + filename));
            Map<String, Course> courses = (Map<String, Course>) ois.readObject();
            ois.close();
            return courses;
        } catch (Exception e) {
            System.out.println("Error loading courses backup: " + e.getMessage());
            return new HashMap<>();
        }
    }
    
    public static void listBackupFiles() {
        File backupDir = new File(BACKUP_DIR);
        File[] files = backupDir.listFiles();
        if (files == null || files.length == 0) {
            System.out.println("No backup files found.");
            return;
        }
        
        System.out.println("\nAvailable backup files:");
        for (File file : files) {
            System.out.println("- " + file.getName());
        }
    }
}