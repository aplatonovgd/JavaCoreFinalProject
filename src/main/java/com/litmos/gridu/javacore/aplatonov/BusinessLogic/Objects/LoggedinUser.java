package com.litmos.gridu.javacore.aplatonov.BusinessLogic.Objects;

public class LoggedinUser {

    private int userId;
    private String userEmail;
    private String userPasswordHash;
    private int isBlocked;
    private long sessionCreatedTime;

    public LoggedinUser(int userId, String userEmail, String userPasswordHash, long sessionCreatedTime){
     this.userId = userId;
     this.userEmail = userEmail;
     this.userPasswordHash = userPasswordHash;
     this.isBlocked = isBlocked;
    }

    public int getUserId() {
        return userId;
    }

    public String getUserPasswordHash() {
        return userPasswordHash;
    }

    public long getSessionCreatedTime() {
        return sessionCreatedTime;
    }

    public int getIsBlocked() {
        return isBlocked;
    }
}
