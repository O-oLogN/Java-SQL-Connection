package com.mycompany.loginform.data;

public class Pair {
    private final String username;
    private final String password;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
    
    public Pair(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    @Override
    public String toString() {
        return username + " " + password;
    }
}
