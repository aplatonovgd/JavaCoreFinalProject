package com.litmos.gridu.javacore.aplatonov.BusinessLogic.Objects;

public class UserExistException extends Exception {

   public UserExistException(String message) {
       super("LoggedinUser already exist");
   }

   public UserExistException(String message, Throwable cause) {
       super("LoggedinUser already exist", cause);
    }

}
