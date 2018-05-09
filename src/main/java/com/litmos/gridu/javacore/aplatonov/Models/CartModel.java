package com.litmos.gridu.javacore.aplatonov.Models;

import com.google.gson.annotations.Expose;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Exceptions.ItemNotfoundException;

import java.lang.Object;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class CartModel {

    private final Object cartModelKey = new Object();

    @Expose(serialize = false, deserialize = false)
    private String cartCreatedTime;

    @Expose
    private List<ItemModel> cartItems;

    @Expose
    private String total = "0";

    public CartModel(List<ItemModel> cartItems, String cartCreatedTime){
        this.cartItems = cartItems;
        this.cartCreatedTime = cartCreatedTime;
    }

    public void addItem(ItemModel itemModel){
        synchronized (cartModelKey) {
            Optional<ItemModel> matchedItem = cartItems.stream().filter(x -> x.getProductId().equals(itemModel.getProductId())).findFirst();

            if (matchedItem.isPresent()) {
                Integer quantity = Integer.valueOf(matchedItem.get().getQuantity()) + Integer.valueOf(itemModel.getQuantity());
                matchedItem.get().setQuantity(String.valueOf(quantity));
            } else {
                cartItems.add(itemModel);
            }
            updateOrderAndTotal();
        }
    }

    public List<ItemModel> getCartItems (){
        synchronized (cartModelKey) {
            return this.cartItems;
        }
    }

    public String getCartCreatedTime() {
        synchronized (cartModelKey) {
            return cartCreatedTime;
        }
    }

    public String getCartItemQuantity(String cartItemId) throws ItemNotfoundException {
        synchronized (cartModelKey) {
            Optional<ItemModel> optionalItemModel = cartItems.stream().filter(arg -> arg.getCartItemId().equals(cartItemId)).findFirst();
            if (!optionalItemModel.isPresent()) {
                throw new ItemNotfoundException("Item not found");
            }
            return optionalItemModel.get().getQuantity();
        }
    }

    public String getProductIdByCartItemId(String cartItemId) throws ItemNotfoundException {
        synchronized (cartModelKey) {
            Optional<ItemModel> optionalItemModel = cartItems.stream().filter(arg -> arg.getCartItemId().equals(cartItemId)).findFirst();
            if (!optionalItemModel.isPresent()) {
                throw new ItemNotfoundException("Item not found");
            }
            return optionalItemModel.get().getProductId();
        }
    }

    public void changeCartItemQuantityById(String itemId, String quantity) throws ItemNotfoundException {
        synchronized (cartModelKey) {
            Optional<ItemModel> optionalItemModel = cartItems.stream().filter(arg -> arg.getCartItemId().equals(itemId)).findFirst();

            if (!optionalItemModel.isPresent()) {
                throw new ItemNotfoundException("Item not found");
            }

            optionalItemModel.get().setQuantity(quantity);
            updateOrderAndTotal();
        }
    }

    public String getTotal() {
        synchronized (cartModelKey) {
            return total;
        }
    }


    public void removeCartItemByCartItemId(String cartItemId){
        synchronized (cartModelKey) {
            Iterator<ItemModel> cartListIterator = cartItems.iterator();

            while (cartListIterator.hasNext()) {
                ItemModel item = cartListIterator.next();

                if (item.getCartItemId().equals(cartItemId)) {
                    cartListIterator.remove();
                }
            }
            updateOrderAndTotal();
        }
    }

    public void updateCartCreatedTime(String cartCreatedTime){
        synchronized (cartModelKey) {
            this.cartCreatedTime = cartCreatedTime;
        }
    }


    private void setTotal(String total){
        synchronized (cartModelKey) {
            this.total = total;
        }
    }


    private void updateOrderAndTotal() {
        synchronized (cartModelKey) {
            int i = 0;
            double total = 0;
            Iterator<ItemModel> itemIterator = cartItems.listIterator();
            while (itemIterator.hasNext()) {
                i++;
                ItemModel itemModel = itemIterator.next();
                itemModel.setCartItemId(String.valueOf(i));
                total += Double.parseDouble(itemModel.getSubtotal());
            }
            setTotal(String.format("%.2f",total));
        }
    }
}
