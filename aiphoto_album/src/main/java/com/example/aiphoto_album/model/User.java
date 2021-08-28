package com.example.aiphoto_album.model;

public class User {
    private String userId;
    private String userName;
    private String password;

    @Override
    public String toString() {
        return "User [password=" + password + ", userId=" + userId + ", userName=" + userName + "]";
    }

    public User(String userId, String userName, String password) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
    }
    
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    
}
