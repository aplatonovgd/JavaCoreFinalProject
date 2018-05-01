package com.litmos.gridu.javacore.aplatonov.Models;

import com.google.gson.annotations.Expose;

public class ItemModel {


    @Expose (serialize = false, deserialize =  false)
    private String productId;

    @Expose (serialize = false, deserialize =  false)
    private Double productPrice;

    @Expose
    private String cartItemId;
    @Expose
    private String title;
    @Expose
    private String quantity;
    @Expose
    private String subtotal;


    public ItemModel(String productId, String title, String quantity, String productPrice){
        this.productId = productId;
        this.title = title;
        this.quantity = quantity;
        this.productPrice = Double.valueOf(productPrice);

       calculateAndSetSubtotal(this.productPrice, Integer.valueOf(quantity));
    }

    private void calculateAndSetSubtotal(double productPrice, int quantity){
        productPrice *= quantity;
        this.subtotal= String.valueOf(productPrice);
    }

    public String getQuantity() {
        return quantity;
    }


    public String getSubtotal(){
        return this.subtotal;
    }

    public void setQuantity(String quantity){
        this.quantity = quantity;
        calculateAndSetSubtotal(productPrice, Integer.valueOf(this.quantity));
    }


    public String getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(String cartItemId){
        this.cartItemId = cartItemId;
    }

    public String getProductId() {
        return productId;
    }

}
