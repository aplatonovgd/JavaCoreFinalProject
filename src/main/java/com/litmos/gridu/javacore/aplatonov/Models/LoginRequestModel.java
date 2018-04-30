package com.litmos.gridu.javacore.aplatonov.Models;

import com.google.gson.annotations.Expose;

public class LoginRequestModel extends RegisterRequestModel {


    @Expose
    private int userId;

    public LoginRequestModel(String email, String password, int userId) {
        super(email, password);
    }

    public int getUserId() {
        return userId;
    }
}
