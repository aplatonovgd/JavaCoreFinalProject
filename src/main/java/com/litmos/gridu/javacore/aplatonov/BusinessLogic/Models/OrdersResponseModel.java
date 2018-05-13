package com.litmos.gridu.javacore.aplatonov.BusinessLogic.Models;

import com.google.gson.annotations.Expose;

import java.util.List;

public class OrdersResponseModel {

    @Expose
    private List<OrderModel> ordersList;

    public OrdersResponseModel(List<OrderModel> ordersList){
        this.ordersList = ordersList;
    }

}
