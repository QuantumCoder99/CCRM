import java.io.Serializable;
import java.util.Set;
import java.util.HashSet;

class Course implements Serializable {
    private String courseId;
    private String courseName;
    private String description;
    private int credits;
    private String instructor;
    private int maxStudents;
    private Set<String> enrolledStudents;
    
    public Course(String courseId, String courseName, String description, int credits, int maxStudents) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.description = description;
        this.credits = credits;
        this.maxStudents = maxStudents;
        this.enrolledStudents = new HashSet<>();
    }
    
    // Getters and Setters
    public String getCourseId() { return courseId; }
    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public int getCredits() { return credits; }
    public void setCredits(int credits) { this.credits = credits; }
    public String getInstructor() { return instructor; }
    public void setInstructor(String instructor) { this.instructor = instructor; }
    public int getMaxStudents() { return maxStudents; }
    public void setMaxStudents(int maxStudents) { this.maxStudents = maxStudents; }
    public Set<String> getEnrolledStudents() { return enrolledStudents; }
    
    public boolean addStudent(String studentId) {
        if (enrolledStudents.size() < maxStudents) {
            return enrolledStudents.add(studentId);
        }
        return false;
    }
    
    public boolean removeStudent(String studentId) {
        return enrolledStudents.remove(studentId);
    }
    
    public int getEnrolledCount() {
        return enrolledStudents.size();
    }
    
    public boolean isFull() {
        return enrolledStudents.size() >= maxStudents;
    }
    
    @Override
    public String toString() {
        return String.format("Course ID: %s | Name: %s | Instructor: %s | Credits: %d | Enrolled: %d/%d", 
                           courseId, courseName, instructor, credits, getEnrolledCount(), maxStudents);
    }
}