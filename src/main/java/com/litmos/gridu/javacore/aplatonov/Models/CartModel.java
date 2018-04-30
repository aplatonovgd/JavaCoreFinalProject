package com.litmos.gridu.javacore.aplatonov.Models;

import com.google.gson.annotations.Expose;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Objects.Item;

import java.util.Iterator;
import java.util.List;

public class CartModel {

    @Expose(serialize = false, deserialize = false)
    private String cartCreatedTime;

    @Expose
    private List<Item> cartItems;

    @Expose
    private String total = "0";

    public CartModel(List<Item> cartItems, String cartCreatedTime){
        this.cartItems = cartItems;
        this.cartCreatedTime = cartCreatedTime;
    }

    public void addItem(Item item){
        cartItems.add(item);
        updateOrderAndTotal();
    }

    public String getTotal() {
        return total;
    }

    public void removeItem(Item item){
        cartItems.remove(item);
        updateOrderAndTotal();
    }

    public void replaceItem(int index, Item item){
        cartItems.set(index, item);
        updateOrderAndTotal();
    }

    private void setTotal(String total){
        this.total = total;
    }


    //TODO REMOVE COMMENTS
    private void updateOrderAndTotal(){
        int i = 0;
        double total =0;
        Iterator<Item> itemIterator = cartItems.listIterator();
        while (itemIterator.hasNext()){
            i++;
            Item item = itemIterator.next();//.setCartItemId(String.valueOf(i));
            item.setCartItemId(String.valueOf(i));
            total += Double.parseDouble(item.getSubtotal());
           // total += Double.parseDouble(itemIterator..getSubtotal());
        }
        setTotal(String.valueOf(total));
    }

}
