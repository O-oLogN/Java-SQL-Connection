package com.mycompany.loginform.data;

public class Lecturer {

    public String getLecturerName() {
        return lecturerName;
    }

    public void setLecturerName(String lecturerName) {
        this.lecturerName = lecturerName;
    }

    public String getLecturerID() {
        return lecturerID;
    }

    public void setLecturerID(String lecturerID) {
        this.lecturerID = lecturerID;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }
    String lecturerName, lecturerID, schedule;

    public Lecturer(String lecturerName, String lecturerID, String schedule) {
        this.lecturerName = lecturerName;
        this.lecturerID = lecturerID;
        this.schedule = schedule;
    }
}
