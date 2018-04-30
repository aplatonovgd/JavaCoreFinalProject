package com.litmos.gridu.javacore.aplatonov.BusinessLogic.Processors.Request;

import com.google.gson.Gson;
import com.litmos.gridu.javacore.aplatonov.Database.DBProcessor;
import com.litmos.gridu.javacore.aplatonov.Models.ProductModel;
import com.litmos.gridu.javacore.aplatonov.Models.RootResponseModel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class RootRequestProccessor {

    private HttpServletRequest request;
    private DBProcessor dbProcessor;
    private Gson gson = new Gson();

    public RootRequestProccessor(HttpServletRequest request, DBProcessor dbProcessor) throws IOException {
        this.request = request;
        this.dbProcessor = dbProcessor;
    }

    public String processRequest() throws SQLException {
        RootResponseModel rootResponseModel= new RootResponseModel(dbProcessor.getProducts());
        return gson.toJson(rootResponseModel);
    }


}
