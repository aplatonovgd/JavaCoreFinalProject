package com.litmos.gridu.javacore.aplatonov.models;

public class LoginRequestModel extends RegisterRequestModel {
    private int userId;
    public LoginRequestModel(String email, String password, int userId) {
        super(email, password);
    }

    public int getUserId() {
        return userId;
    }
}
