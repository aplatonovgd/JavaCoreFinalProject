package com.litmos.gridu.javacore.aplatonov.Models;

import com.google.gson.annotations.Expose;

public class RemoveCartItemRequestModel {

    @Expose
    private String id;

    public RemoveCartItemRequestModel(String id){
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
