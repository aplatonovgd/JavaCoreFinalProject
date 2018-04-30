package com.litmos.gridu.javacore.aplatonov.Models;

import com.google.gson.annotations.Expose;

public class ProductModel {

    @Expose
    private String id;
    @Expose
    private String title;
    @Expose
    private String available;
    @Expose
    private String price;

    public ProductModel(String productId, String productTitle, String productQuantity, String productPrice){
        this.id = productId;
        this.title = productTitle;
        this.available = productQuantity;
        this.price = productPrice;
    }
}
