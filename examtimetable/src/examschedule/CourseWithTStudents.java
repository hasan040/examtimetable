package examschedule;

public class CourseWithTStudents {
    private int CourseID;
    private int EnrolledStudents;

    public CourseWithTStudents(int courseID, int enrolledStudents) {
        CourseID = courseID;
        EnrolledStudents = enrolledStudents;
    }

    public CourseWithTStudents() {
    }

    public int getCourseID() {
        return CourseID;
    }

    public void setCourseID(int courseID) {
        CourseID = courseID;
    }

    public int getEnrolledStudents() {
        return EnrolledStudents;
    }

    public void setEnrolledStudents(int enrolledStudents) {
        EnrolledStudents = enrolledStudents;
    }
}
