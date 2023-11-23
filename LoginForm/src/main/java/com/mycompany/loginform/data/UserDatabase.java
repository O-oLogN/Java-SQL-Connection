package com.mycompany.loginform.data;
import java.sql.*;
import java.util.ArrayList;

public class UserDatabase {
    private ArrayList<Pair> userList = new ArrayList();
    private ArrayList<CourseRegistration> courseRegisteredList = new ArrayList();
    private Connection connection = null;        
    
    public UserDatabase() {
        String serverName = "ologn";
        String databaseName = "User_DB";
        Boolean trustCertificate = true;
        Boolean encrypt = false;
        Boolean integratedSecurity = true;
        String connectionUrl = "jdbc:sqlserver://" + serverName 
                                + ";databaseName=" + databaseName
                                + ";trustServerCertificate=" + trustCertificate.toString()
                                + ";encrypt=" + encrypt.toString()
                                + ";integratedSecurity=" + integratedSecurity.toString();
        String sqlGetUserList = "SELECT * FROM StudentInfo";

        try {
            this.connection = DriverManager.getConnection(connectionUrl);
            System.out.println("Successfully connected to User_DB Database");
            PreparedStatement getUserList = this.connection.prepareStatement(sqlGetUserList);
            ResultSet resultSet = getUserList.executeQuery();
            while (resultSet.next()) {
                String username = resultSet.getString("studentID");
                String password = resultSet.getString("password");
                this.userList.add(new Pair(username.toLowerCase().trim(), password.toLowerCase().trim()));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public ArrayList<Pair> getUserList() {
        return this.userList;
    }
    
    public Connection getConnection() {
        return this.connection;
    }
    
    public ArrayList<CourseRegistration> LoadStudentRegisterdCourseList(String studentID) {
        String sqlGetRegisteredCourseList = """
                SELECT cr.studentID, c.courseID, c.courseName, l.lecturerName, te.timePeriod
                FROM CourseRegistration cr
                JOIN Course c ON c.courseID = cr.courseID
                JOIN Lecturer l ON l.lecturerID = cr.lecturerID
                JOIN TeachingEnrollment te ON te.lecturerID = cr.lecturerID AND te.courseID = cr.courseID
                WHERE cr.studentID = '""" + studentID + 
                "' GROUP BY cr.studentID, c.courseID, c.courseName, l.lecturerName, te.timePeriod";
        String sqlDeleteStudentRecords = "DELETE FROM CourseRegistration\nWHERE studentID = '" + studentID + "'";

        try {
            PreparedStatement getRegisteredCourseList = this.connection.prepareStatement(sqlGetRegisteredCourseList);
            ResultSet resultSet = getRegisteredCourseList.executeQuery();
            while (resultSet.next()) {
                String courseID = resultSet.getString("courseID");
                String courseName = resultSet.getString("courseName");
                String lecturerName = resultSet.getString("lecturerName");
                String schedule = resultSet.getString("timePeriod");
                this.courseRegisteredList.add(new CourseRegistration(courseID, courseName, lecturerName, schedule));
            }            
            PreparedStatement deleteStudentRecords = this.connection.prepareStatement(sqlDeleteStudentRecords);
            deleteStudentRecords.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return this.courseRegisteredList;
    }
}
