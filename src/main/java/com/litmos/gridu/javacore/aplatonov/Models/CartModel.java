package com.litmos.gridu.javacore.aplatonov.Models;

import com.google.gson.annotations.Expose;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Exceptions.ItemNotfoundException;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class CartModel {

    //TODO REMOVE
    Object key1 = new Object();


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
        cartItems.add(itemModel);
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



    private void setTotal(String total){
        this.total = total;
    }


    //TODO REMOVE COMMENTS
    private void updateOrderAndTotal(){
        int i = 0;
        double total =0;
        Iterator<ItemModel> itemIterator = cartItems.listIterator();
        while (itemIterator.hasNext()){
            i++;
            ItemModel itemModel = itemIterator.next();//.setCartItemId(String.valueOf(i));
            itemModel.setCartItemId(String.valueOf(i));
            total += Double.parseDouble(itemModel.getSubtotal());
           // total += Double.parseDouble(itemIterator..getSubtotal());
        }
        setTotal(String.valueOf(total));
    }

}
