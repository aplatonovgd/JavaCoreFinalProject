package com.litmos.gridu.javacore.aplatonov.BusinessLogic.Models;

import com.google.gson.annotations.Expose;

import java.util.List;

public class RootResponseModel {

    @Expose
    private List<ProductModel> products;

    public RootResponseModel(List<ProductModel> products){
        this.products = products;
    }

}
