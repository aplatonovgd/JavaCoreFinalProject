package com.litmos.gridu.javacore.aplatonov.BusinessLogic.Objects;

public class User {

    private String userId;
    private String userSessionId;
    private String userSessionCreatedTime;
    private String userEmail;
    private String userPasswordHash;
    private int isBlocked;

    public User(String userId, String userEmail, String userPasswordHash, int isBlocked){
     this.userId = userId;
     this.userEmail = userEmail;
     this.userPasswordHash = userPasswordHash;
     this.isBlocked = isBlocked;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserSessionId() {
        return userSessionId;
    }

    public String getUserSessionCreatedTime() {
        return userSessionCreatedTime;
    }



}
