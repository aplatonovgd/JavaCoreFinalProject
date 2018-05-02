package com.litmos.gridu.javacore.aplatonov.BusinessLogic.Processors.RequestAndOther;

import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Exceptions.IncorrectQuantityException;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Exceptions.InvalidJsonException;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Exceptions.ItemNotfoundException;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Exceptions.SessionNotFoundException;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Helpers.RequestHelper;
import com.litmos.gridu.javacore.aplatonov.Database.DBProcessor;
import com.litmos.gridu.javacore.aplatonov.Models.CartModel;
import com.litmos.gridu.javacore.aplatonov.Models.ModifyCartItemRequestModel;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class ModifyCartItemRequestProcessor extends AbstractCartRequestProcessor  {

    public ModifyCartItemRequestProcessor(HttpServletRequest request, DBProcessor dbProcessor, CartInfo cartInfo, ProductInfo productInfo,
                                          LoginRequestProcessor.LoggedInUserInfo loggedInUserInfo) throws IOException {
        super(request, dbProcessor, cartInfo, productInfo, loggedInUserInfo);
    }

    public void processRequest() throws InvalidJsonException, SessionNotFoundException, IncorrectQuantityException, ItemNotfoundException {
        ModifyCartItemRequestModel modifyCartItemRequestModel = parseJson(requestBody);

        String userId = RequestHelper.getUserIdByCookies(request.getCookies(),loggedInUserInfo);

        CartModel cartModel = getCartModel(userId);

        modifyItemQuantityInCart(cartModel,modifyCartItemRequestModel.getId(),modifyCartItemRequestModel.getQuantity());
    }


    public void modifyItemQuantityInCart(CartModel cartModel, String cartItemId, String quantity) throws ItemNotfoundException, IncorrectQuantityException {

        int currentCartItemQuantity = Integer.valueOf(cartModel.getCartItemQuantity(cartItemId));
        int expectedQuantity = Integer.valueOf(quantity);

        if(Integer.valueOf(quantity) <= 0) {
            throw new IncorrectQuantityException("Quantity should be bigger than zero");
        }

        String productId = cartModel.getProductIdByCartItemId(cartItemId);

        if(currentCartItemQuantity < expectedQuantity ){

            /* WITHDRAW FROM PRODUCTS CACHE. NOT FROM THE CART*/
            productInfo.withdrawProductQuantity(productId,String.valueOf(expectedQuantity- currentCartItemQuantity));
        }
        else if (currentCartItemQuantity > expectedQuantity){

            /* ADD TO PRODUCTS CACHE. NOT TO THE CART*/
            productInfo.addProductQuantity(productId,String.valueOf(currentCartItemQuantity - expectedQuantity));
        }

        cartModel.changeCartItemQuantityById(cartItemId, quantity);
        cartModel.updateCartCreatedTime(String.valueOf(RequestHelper.getCreationTimeMillis()));
    }

    @Override
    protected ModifyCartItemRequestModel parseJson(String json) throws InvalidJsonException {
        ModifyCartItemRequestModel modifyCartItemRequestModel;

        try{
            modifyCartItemRequestModel =  gson.fromJson(json, ModifyCartItemRequestModel.class);

            if (modifyCartItemRequestModel == null ||  modifyCartItemRequestModel.getId() == null || modifyCartItemRequestModel.getQuantity() == null){
                throw new InvalidJsonException("Invalid JSON");
            }
        }
        catch (Exception e){
            throw new InvalidJsonException(e.getMessage());
        }

        return modifyCartItemRequestModel;
    }
}
