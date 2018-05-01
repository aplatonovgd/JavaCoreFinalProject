package com.litmos.gridu.javacore.aplatonov.Models;

import com.google.gson.annotations.Expose;

public class CheckoutResponseModel {

    @Expose
    private String orderId;

    public CheckoutResponseModel(String orderId){
        this.orderId = orderId;
    }

}
