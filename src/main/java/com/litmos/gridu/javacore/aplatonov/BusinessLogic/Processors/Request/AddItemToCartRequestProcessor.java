package com.litmos.gridu.javacore.aplatonov.BusinessLogic.Processors.Request;

import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Exceptions.*;
import com.litmos.gridu.javacore.aplatonov.Models.ItemModel;
import com.litmos.gridu.javacore.aplatonov.Database.DBProcessor;
import com.litmos.gridu.javacore.aplatonov.Models.AddItemToCartRequestModel;
import com.litmos.gridu.javacore.aplatonov.Models.CartModel;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.SQLException;

public class AddItemToCartRequestProcessor extends AbstractCartRequestProcessor {


    public AddItemToCartRequestProcessor(HttpServletRequest request, DBProcessor dbProcessor, CartInfo cartInfo, ProductInfo productInfo, LoginRequestProcessor.LoggedInUserInfo loggedInUserInfo) throws IOException {
        super(request, dbProcessor, cartInfo, productInfo,loggedInUserInfo);
    }

    public void processRequest() throws InvalidJsonException, SQLException, ItemNotfoundException, IncorrectQuantityException, SessionNotFoundException {

        AddItemToCartRequestModel addItemToCartRequestModel = parseJson(requestBody);

        ItemModel itemModel = productInfo.getItemFromProductInfo(addItemToCartRequestModel.getId()); //getItemFromDatabse(Integer.parseInt(addItemToCartRequestModel.getId()));

        //checkItemQuantity(addItemToCartRequestModel.getQuantity(), itemModel.getQuantity());
        itemModel.setQuantity(addItemToCartRequestModel.getQuantity());

        String userId = getUserIdByCookies(request.getCookies());


        CartModel cartModel = getCartModel(userId);

        addItemToCart(itemModel,userId,cartModel);
    }


    protected void addItemToCart(ItemModel itemModel, String userId, CartModel cartModel) throws ItemNotfoundException, IncorrectQuantityException {

        cartModel.addItem(itemModel);
        cartInfo.addToCartInfo(userId,cartModel);
        productInfo.withdrawProductQuantity(itemModel.getProductId(),itemModel.getQuantity());
    }



//TODO DELETE
  /*  protected void checkItemQuantity(String requestedQuantity, String actualQuantity) throws IncorrectQuantityException {

        int reqQuantity = Integer.parseInt(requestedQuantity);
        int actQuantity = Integer.parseInt(actualQuantity);

        if (actQuantity < reqQuantity){
            throw new IncorrectQuantityException("Request has incorrect quantity");
        }
    }*/


    protected ItemModel getItemFromDatabse(int productId) throws SQLException, ItemNotfoundException {
        ItemModel itemModel = dbProcessor.getItemListFromDatabseById(productId).get(0);
        if (itemModel == null){
            throw new ItemNotfoundException("ProductId not found");
        }
        return itemModel;
    }


    @Override
    protected AddItemToCartRequestModel parseJson(String json) throws InvalidJsonException {
        AddItemToCartRequestModel addItemToCartRequestModel =  gson.fromJson(json, AddItemToCartRequestModel.class);

        if (addItemToCartRequestModel == null ||  addItemToCartRequestModel.getId() == null || addItemToCartRequestModel.getQuantity() == null){
            throw new InvalidJsonException("Invalid JSON");
        }

        return addItemToCartRequestModel;
    }
}
