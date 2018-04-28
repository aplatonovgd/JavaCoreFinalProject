package com.litmos.gridu.javacore.aplatonov.BusinessLogic.Objects;

public class User {

    private String userId;
    private String userSessionId;
    private String userSessionCreatedTime;

    public User(String userId, String userSessionId, String userSessionCreatedTime){
     this.userId = userId;
     this.userSessionId = userSessionId;
     this.userSessionCreatedTime = userSessionCreatedTime;
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
