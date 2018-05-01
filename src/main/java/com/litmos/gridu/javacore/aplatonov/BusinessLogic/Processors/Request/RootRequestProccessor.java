package com.litmos.gridu.javacore.aplatonov.BusinessLogic.Processors.Request;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.litmos.gridu.javacore.aplatonov.Models.RootResponseModel;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.SQLException;


public class RootRequestProccessor {

    private HttpServletRequest request;
    private Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    private AbstractCartRequestProcessor.ProductInfo productInfo;

    public RootRequestProccessor(HttpServletRequest request,AbstractCartRequestProcessor.ProductInfo productInfo) throws IOException {
        this.request = request;
        this.productInfo = productInfo;
    }

    public String processRequest() throws SQLException {
        RootResponseModel rootResponseModel= new RootResponseModel(productInfo.getProductModelList());
        return gson.toJson(rootResponseModel);
    }


}
