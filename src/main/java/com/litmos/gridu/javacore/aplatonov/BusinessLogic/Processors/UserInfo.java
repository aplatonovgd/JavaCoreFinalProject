package com.litmos.gridu.javacore.aplatonov.BusinessLogic.Processors;

import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Objects.User;

import java.util.ArrayList;
import java.util.List;

public class UserInfo {

    private List<User> userList = new ArrayList<>();

    public List<User> getUserList() {
        return userList;
    }

    public void addUser(User user){
       //super.getBody();

        userList.add(user);
    }

}
