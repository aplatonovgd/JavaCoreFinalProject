package com.litmos.gridu.javacore.aplatonov.BusinessLogic.Exceptions;

public class UserExistException extends Exception {

   public UserExistException(String message) {
       super("User already exist");
   }

   public UserExistException(String message, Throwable cause) {
       super("User already exist", cause);
    }

}
