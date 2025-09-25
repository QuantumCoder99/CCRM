import java.util.*;
import java.io.Serializable;

class Student implements Serializable {
    private String studentId;
    private String firstName;
    private String lastName;
    private String email;
    private Date enrollmentDate;
    private Set<String> enrolledCourses;
    private Map<String, Double> grades; // courseId -> grade
    
    public Student(String studentId, String firstName, String lastName, String email) {
        this.studentId = studentId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.enrollmentDate = new Date();
        this.enrolledCourses = new HashSet<>();
        this.grades = new HashMap<>();
    }
    
    // Getters and Setters
    public String getStudentId() { return studentId; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Date getEnrollmentDate() { return enrollmentDate; }
    public Set<String> getEnrolledCourses() { return enrolledCourses; }
    public Map<String, Double> getGrades() { return grades; }
    
    public String getFullName() {
        return firstName + " " + lastName;
    }
    
    public void enrollInCourse(String courseId) {
        enrolledCourses.add(courseId);
    }
    
    public void unenrollFromCourse(String courseId) {
        enrolledCourses.remove(courseId);
        grades.remove(courseId);
    }
    
    public void setGrade(String courseId, double grade) {
        if (enrolledCourses.contains(courseId)) {
            grades.put(courseId, grade);
        }
    }
    
    public double calculateGPA() {
        if (grades.isEmpty()) return 0.0;
        double total = 0.0;
        for (double grade : grades.values()) {
            total += grade;
        }
        return total / grades.size();
    }
    
    @Override
    public String toString() {
        return String.format("Student ID: %s | Name: %s | Email: %s | GPA: %.2f", 
                           studentId, getFullName(), email, calculateGPA());
    }
}