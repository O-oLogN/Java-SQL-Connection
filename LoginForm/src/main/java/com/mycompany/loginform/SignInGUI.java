package com.mycompany.loginform;
import com.mycompany.loginform.data.Pair;
import com.mycompany.loginform.data.UserDatabase;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class SignInGUI extends javax.swing.JFrame {
    private ArrayList<Pair> userList = new ArrayList<>();
    private UserDatabase userDatabase = null;
    public SignInGUI() {
        initComponents();
        this.userDatabase = new UserDatabase();
        this.userList = userDatabase.getUserList();
        signinBtnActionListener();
        showPasswordChbActionListener();
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        studentIdTf = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        signinBtn = new javax.swing.JButton();
        passwordTf = new javax.swing.JPasswordField();
        showPasswordChb = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel1.setText("Student ID");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setText("Password");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel3.setText("Login Form");

        signinBtn.setText("Sign in");

        showPasswordChb.setText("Hint");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 47, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addGap(56, 56, 56)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(studentIdTf, javax.swing.GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE)
                            .addComponent(passwordTf))
                        .addGap(18, 18, 18)
                        .addComponent(showPasswordChb, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(signinBtn)
                        .addGap(210, 210, 210))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addGap(171, 171, 171))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addGap(60, 60, 60)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(studentIdTf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(passwordTf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(showPasswordChb))
                .addGap(35, 35, 35)
                .addComponent(signinBtn)
                .addContainerGap(83, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void showPasswordChbActionListener() {
        showPasswordChb.addActionListener((event) -> {
            if (showPasswordChb.isSelected()) {
                passwordTf.setEchoChar((char)0);
            }
            else {
                passwordTf.setEchoChar('*');
            }
        });
    }
    
    private void signinBtnActionListener() {
        signinBtn.addActionListener((var event) -> {
            String studentID = studentIdTf.getText().trim().toLowerCase();
            passwordTf.setEchoChar('*');
            String password = passwordTf.getText();
            try {
                if (studentID.equals("") || password.equals("")) {
                    throw new IOException ("Please fill all fields!");
                }
                boolean found = false;
                for (Pair student : this.userList) {
                    String username = student.getUsername();
                    String passwordUser = student.getPassword();
                    if (username.equals(studentID) && passwordUser.equals(password)) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    throw new IOException ("Login failed due to incorrect informations!");
                }
                RegistrationGUI registrationGUI = new RegistrationGUI(this.userDatabase, studentID);
                registrationGUI.setVisible(true);
            }
            catch (Exception e) {
                JOptionPane.showMessageDialog(this, e);
                if (studentID.equals("")) {
                    studentIdTf.requestFocus();
                }
                else {
                    passwordTf.requestFocus();
                }
            }
        });
    }
    public static void main() {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SignInGUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPasswordField passwordTf;
    private javax.swing.JCheckBox showPasswordChb;
    private javax.swing.JButton signinBtn;
    private javax.swing.JTextField studentIdTf;
    // End of variables declaration//GEN-END:variables

}
