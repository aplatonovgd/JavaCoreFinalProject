package com.litmos.gridu.javacore.aplatonov.BusinessLogic.Processors.Request;

import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Exceptions.IncorrectQuantityException;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Exceptions.InvalidJsonException;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Exceptions.ItemNotfoundException;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Exceptions.SessionNotFoundException;
import com.litmos.gridu.javacore.aplatonov.Database.DBProcessor;
import com.litmos.gridu.javacore.aplatonov.Models.CartModel;
import com.litmos.gridu.javacore.aplatonov.Models.RemoveCartItemRequestModel;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class RemoveCartItemRequestProcessor extends AbstractCartRequestProcessor {


    public RemoveCartItemRequestProcessor(HttpServletRequest request, DBProcessor dbProcessor, CartInfo cartInfo, ProductInfo productInfo, LoginRequestProcessor.LoggedInUserInfo loggedInUserInfo) throws IOException {
        super(request, dbProcessor, cartInfo, productInfo, loggedInUserInfo);
    }

    public void processRequest() throws InvalidJsonException, SessionNotFoundException, IncorrectQuantityException, ItemNotfoundException {
        RemoveCartItemRequestModel removeCartItemRequestModel = parseJson(requestBody);
        String userId = getUserIdByCookies(request.getCookies());

        CartModel cartModel = getCartModel(userId);

        removeCartItemFromCart(cartModel,removeCartItemRequestModel.getId());
    }




    @Override
    protected RemoveCartItemRequestModel parseJson(String json) throws InvalidJsonException {

        RemoveCartItemRequestModel removeCartItemRequestModel =  gson.fromJson(json, RemoveCartItemRequestModel.class);

        if (removeCartItemRequestModel == null ||  removeCartItemRequestModel.getId() == null){
            throw new InvalidJsonException("Invalid JSON");
        }

        return removeCartItemRequestModel;

    }
}
