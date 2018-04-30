package com.litmos.gridu.javacore.aplatonov.BusinessLogic.Processors.Request;

import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Exceptions.*;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Helpers.RequestHelper;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Objects.Item;
import com.litmos.gridu.javacore.aplatonov.Database.DBProcessor;
import com.litmos.gridu.javacore.aplatonov.Models.CartModel;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DisplayCartRequestProcessor extends AbstractCartRequestProcessor {

    public DisplayCartRequestProcessor(HttpServletRequest request, DBProcessor dbProcessor, CartInfo cartInfo) throws IOException {
        super(request, dbProcessor, cartInfo);
    }

    public String processRequest() throws SessionNotFoundException {
        String jsonBody;

        String userId = getUserIdByCookies(request.getCookies());

        CartModel cartModel;
        try {
             cartModel = cartInfo.getCartByUserId(userId);
        } catch (CartNotFoundException e) {
            return gson.toJson(new CartModel(new ArrayList<Item>(),"0"));
        }
        return gson.toJson(cartModel);
    }

    protected String getUserIdByCookies(Cookie[] cookies) throws SessionNotFoundException {
        List<Cookie> cookieList = RequestHelper.getCookiesList(cookies);

        if (cookieList == null){
            throw new SessionNotFoundException("session not found");
        }

        return RequestHelper.getRequestSessionId(cookieList);
    }

    protected Object parseJson(String json) throws InvalidJsonException {
        return null;
    }
}
