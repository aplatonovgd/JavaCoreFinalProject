package com.litmos.gridu.javacore.aplatonov.models;

public class RegisterRequestModel {

    protected String email;
    protected String password;

    public RegisterRequestModel(String email, String password){
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
