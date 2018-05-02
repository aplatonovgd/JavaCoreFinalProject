package com.litmos.gridu.javacore.aplatonov.BusinessLogic.Processors.RequestAndOther;

import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Exceptions.InvalidJsonException;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Exceptions.SessionNotFoundException;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Helpers.RequestHelper;
import com.litmos.gridu.javacore.aplatonov.Database.DBProcessor;
import com.litmos.gridu.javacore.aplatonov.Models.OrderModel;
import com.litmos.gridu.javacore.aplatonov.Models.OrdersResponseModel;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DisplayOrdersRequestProcessor extends AbstractPostRequestProcessor {

    private LoginRequestProcessor.LoggedInUserInfo loggedInUserInfo;

    public DisplayOrdersRequestProcessor(HttpServletRequest request, DBProcessor dbProcessor, LoginRequestProcessor.LoggedInUserInfo loggedInUserInfo) throws IOException {
        super(request, dbProcessor);
        this.loggedInUserInfo = loggedInUserInfo;
    }

    public String processRequest() throws SQLException, SessionNotFoundException {

        String userId = RequestHelper.getUserIdByCookies(request.getCookies(), loggedInUserInfo);
        List<OrderModel> orderModelList = dbProcessor.getOrdersListByUserId(userId);

        String response;
        if(orderModelList.size() == 0) {
            return gson.toJson(new OrdersResponseModel(new ArrayList<>()));
        }
        else {
            OrdersResponseModel ordersResponseModel = new OrdersResponseModel(orderModelList);

            response = gson.toJson(ordersResponseModel, OrdersResponseModel.class);
            return response;
        }
    }

    @Override
    protected Object parseJson(String json) throws InvalidJsonException {
        return null;
    }
}
