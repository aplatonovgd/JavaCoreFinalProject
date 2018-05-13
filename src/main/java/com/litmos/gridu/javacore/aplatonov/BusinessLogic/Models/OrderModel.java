package com.litmos.gridu.javacore.aplatonov.BusinessLogic.Models;

import com.google.gson.annotations.Expose;

public class OrderModel {

    @Expose(serialize = false, deserialize = false)
    private int userId;

    @Expose
    private String orderId;
    @Expose
    private String orderIndex;
    @Expose
    private String total;
    @Expose
    private String orderDate;
    @Expose
    private String status;

    public OrderModel(String orderId, int userId,String orderIndex, String total, String orderDate, String status){
        this.orderId = orderId;
        this.userId = userId;
        this.orderDate = orderDate;
        this.total = total;
        this.orderDate = orderDate;
        this.orderIndex = orderIndex;
        this.status = status;
    }

    public void setOrderIndex(String orderIndex){
        this.orderIndex = orderIndex;
    }

}
