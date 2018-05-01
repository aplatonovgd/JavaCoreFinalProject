package com.litmos.gridu.javacore.aplatonov.Models;

import com.google.gson.annotations.Expose;

public class ProductModel {

    @Expose
    private String id;

    @Expose
    private String title;

    @Expose
    private String available;

    @Expose(serialize =false, deserialize = false)
    private String availableInDataBase;


    @Expose
    private String price;

    public ProductModel(String productId, String productTitle, String productQuantity, String productPrice){
        this.id = productId;
        this.title = productTitle;
        this.availableInDataBase = productQuantity;
        this.available = productQuantity;
        this.price = productPrice;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPrice() {
        return price;
    }

    public String getQuantity() {
        return available;
    }

    public void setQuantity(String quantity) {
        this.available = quantity;
    }

    public void setDatabaseQuantity(String quantity) {
        this.availableInDataBase = quantity;
    }


    public String getAvailableInDataBase() {
        return availableInDataBase;
    }

}
