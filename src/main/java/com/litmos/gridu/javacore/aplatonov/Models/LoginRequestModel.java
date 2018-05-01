package com.litmos.gridu.javacore.aplatonov.Models;

import com.google.gson.annotations.Expose;

public class LoginRequestModel extends RegisterRequestModel {

    public LoginRequestModel(String email, String password, int userId) {
        super(email, password);
    }

}
