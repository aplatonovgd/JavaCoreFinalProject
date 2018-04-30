package com.litmos.gridu.javacore.aplatonov.Models;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public class RootResponseModel {

    @Expose
    private List<ProductModel> products;

    public RootResponseModel(List<ProductModel> products){
        this.products = products;
    }

}
