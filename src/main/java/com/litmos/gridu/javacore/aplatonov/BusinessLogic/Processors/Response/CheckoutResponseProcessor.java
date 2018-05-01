package com.litmos.gridu.javacore.aplatonov.BusinessLogic.Processors.Response;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.litmos.gridu.javacore.aplatonov.Models.CheckoutResponseModel;

public class CheckoutResponseProcessor implements BaseResponseProcessor {

    @Expose
    private String orderId;

    private Gson gson = new Gson();


    public CheckoutResponseProcessor(String orderId){
        this.orderId = orderId;
    }

    @Override
    public String getResponseBody() {

        CheckoutResponseModel checkoutResponseModel = new CheckoutResponseModel(orderId);

        return gson.toJson(checkoutResponseModel, CheckoutResponseModel.class);
    }

}
