package com.litmos.gridu.javacore.aplatonov.BusinessLogic.Objects;

public class ValidationResult {


    private boolean success;
    private String message;
    private String description;


    public ValidationResult(boolean isSuccess){
        this.success = isSuccess;
    }

    public ValidationResult(boolean isSuccess, String message, String description){
        this.success = isSuccess;
        this.message = message;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }
}
