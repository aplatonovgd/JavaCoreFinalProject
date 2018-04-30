package com.litmos.gridu.javacore.aplatonov.BusinessLogic.Processors.Request;

import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Exceptions.*;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Helpers.RequestHelper;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Objects.Item;
import com.litmos.gridu.javacore.aplatonov.Database.DBProcessor;
import com.litmos.gridu.javacore.aplatonov.Models.AddItemToCartRequestModel;
import com.litmos.gridu.javacore.aplatonov.Models.CartModel;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AddItemToCartRequestProcessor extends AbstractCartRequestProcessor {


    public AddItemToCartRequestProcessor(HttpServletRequest request, DBProcessor dbProcessor, CartInfo cartInfo) throws IOException {
        super(request, dbProcessor, cartInfo);
    }

    public void processRequest() throws InvalidJsonException, SQLException, ItemNotfoundException, IncorrectQuantityException, SessionNotFoundException {

        AddItemToCartRequestModel addItemToCartRequestModel = parseJson(requestBody);

        Item item = getItemFromDatabse(Integer.parseInt(addItemToCartRequestModel.getId()));
        checkItemQuantity(addItemToCartRequestModel.getQuantity(),item.getQuantity());
        item.setQuantity(addItemToCartRequestModel.getQuantity());

        String userId = getUserIdByCookies(request.getCookies());

        CartModel cartModel;

        try {
            cartModel = cartInfo.getCartByUserId(userId);
        }
        catch (CartNotFoundException e) {
            cartModel = createCartModel();
        }

        cartModel.addItem(item);

        cartInfo.addToCartInfo(userId,cartModel);
    }


    protected CartModel createCartModel(){
        List<Item> itemList = new ArrayList<>();

        return new CartModel(itemList,String.valueOf(RequestHelper.getCreationTime()));
    }


    protected String getUserIdByCookies(Cookie [] cookies) throws SessionNotFoundException {
        List<Cookie> cookieList = RequestHelper.getCookiesList(cookies);

        if (cookieList == null){
            throw new SessionNotFoundException("session not found");
        }

        return RequestHelper.getRequestSessionId(cookieList);
    }


    protected void checkItemQuantity(String requestedQuantity, String actualQuantity) throws IncorrectQuantityException {

        int reqQuantity = Integer.parseInt(requestedQuantity);
        int actQuantity = Integer.parseInt(actualQuantity);

        if (actQuantity < reqQuantity){
            throw new IncorrectQuantityException("Request has incorect quantity");
        }
    }


    protected Item getItemFromDatabse(int productId) throws SQLException, ItemNotfoundException {
        Item item = dbProcessor.getItemListFromDatabseById(productId).get(0);
        if (item == null){
            throw new ItemNotfoundException("ProductId not found");
        }
        return item;
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
