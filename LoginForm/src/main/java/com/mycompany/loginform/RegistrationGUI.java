package com.mycompany.loginform;
import com.mycompany.loginform.data.CourseRegistration;
import com.mycompany.loginform.data.Lecturer;
import com.mycompany.loginform.data.Pair;
import com.mycompany.loginform.data.UserDatabase;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class RegistrationGUI extends javax.swing.JFrame {
    private final DefaultTableModel registrationTableModel;
    private ArrayList<CourseRegistration> registeredCourseList = new ArrayList<>(), newRegisteredCourseList = new ArrayList<>();
    private final ArrayList<Pair> courseList = new ArrayList<>();
    private final ArrayList<Lecturer> lecturerList = new ArrayList<>();
    private final Connection connection;
    
    public RegistrationGUI(UserDatabase userDatabase, String studentID) {
        initComponents();
        this.registrationTableModel = (DefaultTableModel) registrationTable.getModel();
        this.registrationTableModel.setRowCount(0);
        this.registeredCourseList = userDatabase.LoadStudentRegisterdCourseList(studentID);
        this.newRegisteredCourseList = this.registeredCourseList;
        this.registeredCourseList.forEach((x) -> this.registrationTableModel.addRow(x.toObject()));
        this.connection = userDatabase.getConnection();
        
        loadAllCourses();
        courseNameCbActionListener();
        lecturerNameCbActionListener();
        registerBtnActionListener();
        removeBtnActionListener();
        saveBtnActionListener(studentID);
    }
    
    private void loadAllCourses() {
        String sqlGetAllCourses = "SELECT * FROM Course";
        PreparedStatement getAllCourses;
        courseNameCb.removeAllItems();
        lecturerNameCb.removeAllItems();
        scheduleCb.removeAllItems();            
        try {
            getAllCourses = this.connection.prepareStatement(sqlGetAllCourses);
            ResultSet resultSet = getAllCourses.executeQuery();
            while (resultSet.next()) {
                String courseID = resultSet.getString("courseID");
                String courseName = resultSet.getString("courseName");
                this.courseList.add(new Pair(courseID, courseName));
            }
            this.courseList.forEach((course) -> courseNameCb.addItem(course.getPassword()));

        } 
        catch (SQLException e) {
            e.printStackTrace();
        }   
    }
    
    private void courseNameCbActionListener() {
        courseNameCb.addActionListener((event) -> {
            try {
                String courseName = courseNameCb.getEditor().getItem().toString().trim();
                String courseID = "";
                for (Pair course : this.courseList) {
                    if (course.getPassword().trim().toLowerCase().equals(courseName.toLowerCase())) {
                        courseIdTf.setText(course.getUsername());
                        courseID = course.getUsername();
                        break;
                    }
                }
                this.lecturerList.clear();
                lecturerNameCb.removeAllItems();
                String sqlGetCourseLecturer = """
                      SELECT te.courseID, l.lecturerID, l.lecturerName, te.timePeriod
                      FROM Lecturer l 
                      JOIN TeachingEnrollment te ON te.lecturerID = l.lecturerID
                      AND te.courseID = '""" + courseID +
                      "' GROUP BY te.courseID, l.lecturerID, l.lecturerName, te.timePeriod";
                PreparedStatement getCourseLecturer = this.connection.prepareStatement(sqlGetCourseLecturer);
                ResultSet resultSet = getCourseLecturer.executeQuery();
                while (resultSet.next()) {
                    String lecturerName = resultSet.getString("lecturerName");
                    String lecturerID = resultSet.getString("lecturerID");
                    String schedule = resultSet.getString("timePeriod");
                    this.lecturerList.add(new Lecturer(lecturerName, lecturerID, schedule));
                }
                this.lecturerList.forEach((lecturer) -> lecturerNameCb.addItem(lecturer.getLecturerName()));
            } 
            catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
    
    private void lecturerNameCbActionListener() {
        lecturerNameCb.addActionListener((event) -> {
            String lecturerName = lecturerNameCb.getEditor().getItem().toString();
            scheduleCb.removeAllItems();
            for (Lecturer lecturer : this.lecturerList) {
                if (lecturer.getLecturerName().equals(lecturerName)) {
                    lecturerIdTf.setText(lecturer.getLecturerID());
                    scheduleCb.addItem(lecturer.getSchedule());
                }
            }
        });
    }
    
    private void registerBtnActionListener() {
        registerBtn.addActionListener((event) -> {
            try {
                String courseName = courseNameCb.getSelectedItem().toString();
                String courseID = courseIdTf.getText();
                String lecturerName = lecturerNameCb.getSelectedItem().toString();
                String lecturerID = lecturerIdTf.getText();
                String schedule = scheduleCb.getSelectedItem().toString();
                if (courseName.equals("") || courseID.equals("") || lecturerName.equals("")
                   || lecturerID.equals("") || schedule.equals("")) {
                    throw new IOException ("Please fill all course information!");
                }
                CourseRegistration registeredCourse = new CourseRegistration(courseID, courseName, lecturerName, schedule);
                for (CourseRegistration course : this.newRegisteredCourseList) {
                    if (course.equals(registeredCourse)) {
                        throw new IOException ("You've registered for the same course!");
                    }
                    if (course.getCourseID().equals(registeredCourse.getCourseID())) {
                        this.newRegisteredCourseList.remove(course);
                        for (int row = 0; row < registrationTable.getRowCount(); ++row) {
                            if (registrationTable.getValueAt(row, 0).toString().equals(course.getCourseID())) {
                                this.registrationTableModel.removeRow(row);
                                break;
                            }
                        }
                        break;
                    }
                }
                this.registrationTableModel.addRow(registeredCourse.toObject());
                this.newRegisteredCourseList.add(registeredCourse);
            }
            catch (Exception e) {
                JOptionPane.showMessageDialog(this, e);
            }
        });
    }
    
    private void removeBtnActionListener() {
        removeBtn.addActionListener((event) -> {
            int row = registrationTable.getSelectedRow();
            if (row >= 0 && row < registrationTable.getRowCount()) {
                String courseID = registrationTable.getValueAt(row, 0).toString();
                String courseName = registrationTable.getValueAt(row, 1).toString();
                String lecturerName = registrationTable.getValueAt(row, 2).toString();
                String schedule = registrationTable.getValueAt(row, 3).toString();
                this.registrationTableModel.removeRow(row);
                CourseRegistration removeCourse = new CourseRegistration(courseID, courseName, lecturerName, schedule);
                for (CourseRegistration course : this.newRegisteredCourseList) {
                    if (course.equals(removeCourse)) {
                        this.newRegisteredCourseList.remove(course);
                        break;
                    }
                }
            }
        });
    }
    
    private void saveBtnActionListener(String studentID) {
        saveBtn.addActionListener((event) -> {
            this.newRegisteredCourseList.forEach((course) -> {
                try {
                    String lecturerID = "";
                    for (Lecturer lecturer : this.lecturerList) {
                        if (lecturer.getLecturerName().equals(course.getLecturerName())) {
                            lecturerID = lecturer.getLecturerID();
                            break;
                        }
                    }
                    String sqlUpdate = "INSERT INTO CourseRegistration VALUES ('"
                            + studentID + "','"
                            + course.getCourseID() + "','"
                            + lecturerID + "');";
                    PreparedStatement update = this.connection.prepareStatement(sqlUpdate);
                    update.executeUpdate();
                    this.registrationTableModel.setRowCount(0);
                    registerBtn.setEnabled(false);
                } 
                catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        });
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        courseNameCb = new javax.swing.JComboBox<>();
        lecturerNameCb = new javax.swing.JComboBox<>();
        courseIdTf = new javax.swing.JTextField();
        lecturerIdTf = new javax.swing.JTextField();
        scheduleCb = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        registrationTable = new javax.swing.JTable();
        registerBtn = new javax.swing.JButton();
        removeBtn = new javax.swing.JButton();
        saveBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Course name");

        jLabel2.setText("Course ID");

        jLabel3.setText("Lecturer name");

        jLabel4.setText("Lecturer ID");

        jLabel5.setText("Schedule");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel6.setText("Course Registration");

        courseNameCb.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        lecturerNameCb.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        scheduleCb.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        registrationTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Course ID", "Course Name", "Lecturer", "Schedule"
            }
        ));
        jScrollPane1.setViewportView(registrationTable);

        registerBtn.setText("Register");

        removeBtn.setText("Remove");

        saveBtn.setText("Save");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 903, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 12, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel6)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2)
                            .addComponent(jLabel4)
                            .addComponent(jLabel3)
                            .addComponent(jLabel5))
                        .addGap(192, 192, 192)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(courseIdTf, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(courseNameCb, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lecturerNameCb, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lecturerIdTf, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(scheduleCb, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(saveBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(registerBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(removeBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(106, 106, 106))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addGap(57, 57, 57)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(courseNameCb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(32, 32, 32)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(courseIdTf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(36, 36, 36)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lecturerNameCb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addGap(36, 36, 36)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(lecturerIdTf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(registerBtn)
                        .addGap(52, 52, 52)
                        .addComponent(removeBtn)
                        .addGap(55, 55, 55)
                        .addComponent(saveBtn)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(scheduleCb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 337, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField courseIdTf;
    private javax.swing.JComboBox<String> courseNameCb;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField lecturerIdTf;
    private javax.swing.JComboBox<String> lecturerNameCb;
    private javax.swing.JButton registerBtn;
    private javax.swing.JTable registrationTable;
    private javax.swing.JButton removeBtn;
    private javax.swing.JButton saveBtn;
    private javax.swing.JComboBox<String> scheduleCb;
    // End of variables declaration//GEN-END:variables
}
