package com.mycompany.loginform.data;

public class CourseRegistration {

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getLecturerName() {
        return lecturerName;
    }

    public void setLecturerName(String lecturerName) {
        this.lecturerName = lecturerName;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }
    private String courseID, courseName, lecturerName, schedule;

    public CourseRegistration(String courseID, String courseName, String lecturerName, String schedule) {
        this.courseID = courseID;
        this.courseName = courseName;
        this.lecturerName = lecturerName;
        this.schedule = schedule;
    }
    
    public Object[] toObject() {
        return new Object[] {
            this.courseID, this.courseName, this.lecturerName, this.schedule
        };
    }
}
