package examschedule;

import java.util.List;

public class CourseDetails {
    private int courseIndexInfo;

    private int timeSlotId;

    private List<Integer> enrolledStudentList;

    public CourseDetails(int courseIndexInfo, List<Integer> enrolledStudentList) {
        this.courseIndexInfo = courseIndexInfo;
        this.enrolledStudentList = enrolledStudentList;
        this.timeSlotId = 9999;
    }



    public CourseDetails() {
    }

    public int getTimeSlotId() {
        return timeSlotId;
    }

    public void setTimeSlotId(int timeSlotId) {
        this.timeSlotId = timeSlotId;
    }

    public int getCourseIndexInfo() {
        return courseIndexInfo;
    }

    public void setCourseIndexInfo(int courseIndexInfo) {
        this.courseIndexInfo = courseIndexInfo;
    }

    public List<Integer> getEnrolledStudentList() {
        return enrolledStudentList;
    }

    public void setEnrolledStudentList(List<Integer> enrolledStudentList) {
        this.enrolledStudentList = enrolledStudentList;
    }

    public int getTotalEnrolledStudents(){
        return enrolledStudentList.size();
    }

}
