package com.litmos.gridu.javacore.aplatonov.BusinessLogic.Processors.Request;

import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Exceptions.CartNotFoundException;
import com.litmos.gridu.javacore.aplatonov.Database.DBProcessor;
import com.litmos.gridu.javacore.aplatonov.Models.CartModel;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractCartRequestProcessor extends AbstractPostRequestProcessor {
    CartInfo cartInfo;

    protected AbstractCartRequestProcessor(HttpServletRequest request, DBProcessor dbProcessor, CartInfo cartInfo) throws IOException {
        super(request, dbProcessor);
        this.cartInfo = cartInfo;
    }


    public static class CartInfo {

        private Map<String, CartModel> userIdToCartMap = new HashMap<>();

        public CartModel getCartByUserId(String userId) throws CartNotFoundException {
            CartModel cartModel;
            if (userIdToCartMap.containsKey(userId)) {
                cartModel = userIdToCartMap.get(userId);
            } else {
                throw new CartNotFoundException("Cart not found");
            }
            return cartModel;
        }

        protected void addToCartInfo(String userId, CartModel cartModel) {

            userIdToCartMap.put(userId, cartModel);
        }

        protected void replaceCartInfo(String userId, CartModel cartModel){
            userIdToCartMap.replace(userId,cartModel);
        }


    }
}
