package com.litmos.gridu.javacore.aplatonov.BusinessLogic.Processors.Request;

import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Exceptions.*;
import com.litmos.gridu.javacore.aplatonov.Models.ItemModel;
import com.litmos.gridu.javacore.aplatonov.Database.DBProcessor;
import com.litmos.gridu.javacore.aplatonov.Models.CartModel;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;

public class DisplayCartRequestProcessor extends AbstractCartRequestProcessor {

    public DisplayCartRequestProcessor(HttpServletRequest request, DBProcessor dbProcessor, CartInfo cartInfo, ProductInfo productInfo, LoginRequestProcessor.LoggedInUserInfo loggedInUserInfo) throws IOException {
        super(request, dbProcessor, cartInfo, productInfo, loggedInUserInfo);
    }

    public String processRequest() throws SessionNotFoundException {
        String jsonBody;

        String userId = getUserIdByCookies(request.getCookies());

        CartModel cartModel;
        try {

            cartModel = cartInfo.getCartByUserId(userId);

        } catch (CartNotFoundException e) {

            return gson.toJson(new CartModel(new ArrayList<ItemModel>(),"0"));
        }
        return gson.toJson(cartModel);
    }


    protected Object parseJson(String json) throws InvalidJsonException {
        return null;
    }
}
