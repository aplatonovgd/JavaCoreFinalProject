package com.litmos.gridu.javacore.aplatonov.BusinessLogic.Models;

public class LoginRequestModel extends RegisterRequestModel {

    public LoginRequestModel(String email, String password, int userId) {
        super(email, password);
    }

}
