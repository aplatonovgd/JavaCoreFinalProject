package com.litmos.gridu.javacore.aplatonov.BusinessLogic.Models;

import com.google.gson.annotations.Expose;

public class ModifyCartItemRequestModel {

    @Expose
    private String id;
    @Expose
    private String quantity;

    public ModifyCartItemRequestModel(String id, String quantity){
        this.id = id;
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public String getQuantity() {
        return quantity;
    }



}
