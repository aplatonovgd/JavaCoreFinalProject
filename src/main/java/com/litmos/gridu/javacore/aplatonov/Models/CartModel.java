package com.litmos.gridu.javacore.aplatonov.Models;

import com.google.gson.annotations.Expose;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Exceptions.ItemNotfoundException;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class CartModel {


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
        Optional<ItemModel> matchedItem = cartItems.stream().filter(x -> x.getProductId().equals(itemModel.getProductId())).findFirst();

        if(matchedItem.isPresent()){
            Integer quantity = Integer.valueOf(matchedItem.get().getQuantity()) + Integer.valueOf(itemModel.getQuantity());
            matchedItem.get().setQuantity(String.valueOf(quantity));
        }
        else {
            cartItems.add(itemModel);
        }
        updateOrderAndTotal();
    }

    public List<ItemModel> getCartItems (){
        return this.cartItems;
    }

    public String getCartCreatedTime() {
        return cartCreatedTime;
    }

    public String getCartItemQuantity(String cartItemId) throws ItemNotfoundException {

        Optional<ItemModel> optionalItemModel=  cartItems.stream().filter(arg -> arg.getCartItemId().equals(cartItemId)).findFirst();
        if(!optionalItemModel.isPresent()){
            throw new ItemNotfoundException("Item not found");
        }
        return optionalItemModel.get().getQuantity();
    }

    public String getProductIdByCartItemId(String cartItemId) throws ItemNotfoundException {

        Optional<ItemModel> optionalItemModel=  cartItems.stream().filter(arg -> arg.getCartItemId().equals(cartItemId)).findFirst();
        if(!optionalItemModel.isPresent()){
            throw new ItemNotfoundException("Item not found");
        }
        return optionalItemModel.get().getProductId();
    }

    public void changeCartItemQuantityById(String itemId, String quantity) throws ItemNotfoundException {
        Optional<ItemModel> optionalItemModel=  cartItems.stream().filter(arg -> arg.getCartItemId().equals(itemId)).findFirst();

        if(!optionalItemModel.isPresent()){
            throw new ItemNotfoundException("Item not found");
        }

        optionalItemModel.get().setQuantity(quantity);
        updateOrderAndTotal();
    }

    public String getTotal() {
        return total;
    }


    public void removeCartItemByCartItemId(String cartItemId){

       Iterator<ItemModel> cartListIterator = cartItems.iterator();

           while (cartListIterator.hasNext()) {
               ItemModel item = cartListIterator.next();

               if (item.getCartItemId().equals(cartItemId)) {
                   cartListIterator.remove();
               }
           }
       updateOrderAndTotal();
    }

    public void updateCartCreatedTime(String cartCreatedTime){
        this.cartCreatedTime =cartCreatedTime;
    }


    private void setTotal(String total){
        this.total = total;
    }


    private void updateOrderAndTotal(){
        int i = 0;
        double total =0;
        Iterator<ItemModel> itemIterator = cartItems.listIterator();
        while (itemIterator.hasNext()){
            i++;
            ItemModel itemModel = itemIterator.next();
            itemModel.setCartItemId(String.valueOf(i));
            total += Double.parseDouble(itemModel.getSubtotal());
        }
        setTotal(String.valueOf(total));
    }

}
