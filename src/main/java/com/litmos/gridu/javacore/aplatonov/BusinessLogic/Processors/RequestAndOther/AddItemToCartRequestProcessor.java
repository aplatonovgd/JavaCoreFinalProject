package com.litmos.gridu.javacore.aplatonov.BusinessLogic.Processors.RequestAndOther;

import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Exceptions.*;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Helpers.RequestHelper;
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

        ItemModel itemModel = productInfo.getItemFromProductInfo(addItemToCartRequestModel.getId());

        itemModel.setQuantity(addItemToCartRequestModel.getQuantity());

        String userId = RequestHelper.getUserIdByCookies(request.getCookies(),loggedInUserInfo);

        CartModel cartModel = getCartModel(userId);

        addItemToCart(itemModel,userId,cartModel);
    }


    protected void addItemToCart(ItemModel itemModel, String userId, CartModel cartModel) throws ItemNotfoundException, IncorrectQuantityException {

        if (Integer.valueOf(itemModel.getQuantity()) <= 0){
            throw new IncorrectQuantityException("Quantity should be bigger than zero");
        }
        productInfo.withdrawProductQuantity(itemModel.getProductId(),itemModel.getQuantity());
        cartModel.addItem(itemModel);
        cartModel.updateCartCreatedTime(String.valueOf(RequestHelper.getCreationTimeMillis()));
        cartInfo.addToCartInfo(userId,cartModel);
    }


    protected ItemModel getItemFromDatabse(int productId) throws SQLException, ItemNotfoundException {
        ItemModel itemModel = dbProcessor.getItemListFromDatabseById(productId).get(0);
        if (itemModel == null){
            throw new ItemNotfoundException("ProductId not found");
        }
        return itemModel;
    }


    @Override
    protected AddItemToCartRequestModel parseJson(String json) throws InvalidJsonException {
        AddItemToCartRequestModel addItemToCartRequestModel;
        try{
            addItemToCartRequestModel =  gson.fromJson(json, AddItemToCartRequestModel.class);
            if (addItemToCartRequestModel == null ||  addItemToCartRequestModel.getId() == null || addItemToCartRequestModel.getQuantity() == null){
                throw new InvalidJsonException("Invalid JSON");
            }
        }
        catch (Exception e){
            throw new InvalidJsonException(e.getMessage());
        }

        return addItemToCartRequestModel;
    }
}
