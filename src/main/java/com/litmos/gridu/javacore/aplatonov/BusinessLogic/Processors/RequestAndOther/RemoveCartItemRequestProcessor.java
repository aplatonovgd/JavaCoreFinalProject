package com.litmos.gridu.javacore.aplatonov.BusinessLogic.Processors.RequestAndOther;

import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Exceptions.IncorrectQuantityException;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Exceptions.InvalidJsonException;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Exceptions.ItemNotfoundException;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Exceptions.SessionNotFoundException;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Helpers.RequestHelper;
import com.litmos.gridu.javacore.aplatonov.Database.DBProcessor;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Models.CartModel;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Models.RemoveCartItemRequestModel;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class RemoveCartItemRequestProcessor extends AbstractCartRequestProcessor {


    public RemoveCartItemRequestProcessor(HttpServletRequest request, DBProcessor dbProcessor, CartInfo cartInfo, ProductInfo productInfo, LoginRequestProcessor.LoggedInUserInfo loggedInUserInfo) throws IOException {
        super(request, dbProcessor, cartInfo, productInfo, loggedInUserInfo);
    }

    public void processRequest() throws InvalidJsonException, SessionNotFoundException, IncorrectQuantityException, ItemNotfoundException {
        RemoveCartItemRequestModel removeCartItemRequestModel = parseJson(requestBody);
        String userId = RequestHelper.getUserIdByCookies(request.getCookies(),loggedInUserInfo);

        CartModel cartModel = getCartModel(userId);

        removeCartItemFromCart(cartModel,removeCartItemRequestModel.getId());
        cartModel.updateCartCreatedTime(String.valueOf(RequestHelper.getCreationTimeMillis()));
    }




    @Override
    protected RemoveCartItemRequestModel parseJson(String json) throws InvalidJsonException {

        RemoveCartItemRequestModel removeCartItemRequestModel;

        try {
            removeCartItemRequestModel = gson.fromJson(json, RemoveCartItemRequestModel.class);
            if (removeCartItemRequestModel == null ||  removeCartItemRequestModel.getId() == null){
                throw new InvalidJsonException("Invalid JSON");
            }
        }
        catch (Exception e ){
            throw new InvalidJsonException("Invalid JSON");
        }

        return removeCartItemRequestModel;

    }
}
