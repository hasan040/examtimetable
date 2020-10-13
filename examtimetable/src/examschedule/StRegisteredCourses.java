package examschedule;

import java.util.List;

public class StRegisteredCourses {

    private int studentID;
    private List<Integer> registeredCourses;
    private int totalPenalty;

    public StRegisteredCourses() {
    }

    public StRegisteredCourses(int studentID, List<Integer> registeredCourses) {
        this.studentID = studentID;
        this.registeredCourses = registeredCourses;
        this.totalPenalty = 0;
    }

    public int getTotalPenalty() {
        return totalPenalty;
    }

    public void setTotalPenalty(int totalPenalty) {
        this.totalPenalty = totalPenalty;
    }

    public int getStudentID() {
        return studentID;
    }

    public void setStudentID(int studentID) {
        this.studentID = studentID;
    }

    public List<Integer> getRegisteredCourses() {
        return registeredCourses;
    }

    public void setRegisteredCourses(List<Integer> registeredCourses) {
        this.registeredCourses = registeredCourses;
    }
}
