package com.litmos.gridu.javacore.aplatonov.BusinessLogic.Processors.RequestAndOther;

import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Exceptions.*;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Helpers.RequestHelper;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Models.ItemModel;
import com.litmos.gridu.javacore.aplatonov.Database.DBProcessor;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Models.CartModel;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;

public class DisplayCartRequestProcessor extends AbstractCartRequestProcessor {

    public DisplayCartRequestProcessor(HttpServletRequest request, DBProcessor dbProcessor, CartInfo cartInfo, ProductInfo productInfo, LoginRequestProcessor.LoggedInUserInfo loggedInUserInfo) throws IOException {
        super(request, dbProcessor, cartInfo, productInfo, loggedInUserInfo);
    }

    public String processRequest() throws SessionNotFoundException {
        String jsonBody;

        String userId = RequestHelper.getUserIdByCookies(request.getCookies(),loggedInUserInfo);

        CartModel cartModel;
        try {

            cartModel = cartInfo.getCartByUserId(userId);

        } catch (CartNotFoundException e) {

            return gson.toJson(new CartModel(new ArrayList<ItemModel>(),"0"));
        }
        return gson.toJson(cartModel);
    }


    protected Object parseJson(String json) throws InvalidJsonException {
        throw new NotImplementedException();
    }
}
