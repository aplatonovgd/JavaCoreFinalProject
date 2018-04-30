package com.litmos.gridu.javacore.aplatonov.Database;

import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Objects.Item;
import com.litmos.gridu.javacore.aplatonov.Models.LoginRequestModel;
import com.litmos.gridu.javacore.aplatonov.Models.ProductModel;
import com.litmos.gridu.javacore.aplatonov.Models.RegisterRequestModel;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class DBProcessor extends DBConnector {

    boolean isDbExist= false;


    public DBProcessor(String dbUrl, String dbName, String username, String dbPassword, String dbCreateScript, String addTableDataScript) throws IOException, SQLException {
        initDBConnector(dbUrl, dbName, username, dbPassword);
        try (Connection connection = getDataBaseConnection()) {}
        catch (SQLException e) {
            //MySQL only
            if (e.getSQLState().equalsIgnoreCase("42000")) {
                executeBatchStatementOnServer(dbCreateScript);
                executeBatchStatementOnServer(addTableDataScript);
             }
             else throw new SQLException(e);
        }
    }


    public List<Item> getItemListFromDatabseById(int productId) throws SQLException {
        List<Item> itemList = selectStatementHandler(getItem, "select * from products where productId = ?", productId);
        return itemList;
    }


    public void addUserToDB(RegisterRequestModel registerRequestModel) throws SQLException {

        insertStatementHandler("INSERT INTO `users` (`userLogin`, `userPassword`, `isBlocked`) VALUES (?,?,?)",
                registerRequestModel.getEmail(), registerRequestModel.getPassword(),0);
    }


    public List<ProductModel> getProducts() throws SQLException {
         List<ProductModel> productModels;
         productModels = selectStatementHandler(getProducts,"select * from products");
         return productModels;
    }

    public List<LoginRequestModel> getLoginRequestModelListByLogin(String email) throws SQLException, IllegalArgumentException {
        List<LoginRequestModel> userList;
        userList = selectStatementHandler(getLoginUserModel,"select * from users where userLogin = ?", email);
        return userList;
    }

    public List<RegisterRequestModel> getRegisterRequestModelListByLogin(String email) throws SQLException, IllegalArgumentException {
        List<RegisterRequestModel> userList;
        userList = selectStatementHandler(getRegisteredUsersModel,"select * from users where userLogin = ?", email);
        return userList;
    }




    static CheckedFunction<ResultSet,List<Item>> getItem = resultSet -> {
        List<Item> itemList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                String productId = String.valueOf(resultSet.getInt("productId"));
                String title = resultSet.getString("productTitle");
                String quantity = resultSet.getString("productQuantity");
                String productPrice = resultSet.getString("productPrice");
                Item item = new Item(productId,title,quantity,productPrice);
                itemList.add(item);
            }
        }
        catch (SQLException e){
            throw  e;
        }
        return itemList;
    };


    static CheckedFunction<ResultSet,List<ProductModel>> getProducts = resultSet -> {
        List<ProductModel> productModels = new ArrayList<>();
        try {
            while (resultSet.next()) {

                String productId = String.valueOf(resultSet.getInt("productId"));
                String productTitle = String.valueOf(resultSet.getString("productTitle"));
                String productQuantity = String.valueOf(resultSet.getString("productQuantity"));
                String productPrice = String.valueOf(resultSet.getString("productPrice"));

                ProductModel productModel = new ProductModel(productId,productTitle,productQuantity,productPrice);
                productModels.add(productModel);
            }
        }
        catch (SQLException e){
            throw  e;
        }
        return  productModels;
    };


    static CheckedFunction<ResultSet,List<LoginRequestModel>> getLoginUserModel= resultSet -> {
        List<LoginRequestModel> registeredUsers = new ArrayList<>();
        try {
            while (resultSet.next()) {
                String email = resultSet.getString("userLogin");
                String password = resultSet.getString("userPassword");
                int userId = resultSet.getInt("userId");
                LoginRequestModel registerRequestModel = new LoginRequestModel(email, password, userId);
                registeredUsers.add(registerRequestModel);
            }
        }
        catch (SQLException e){
            throw e;
        }
        return registeredUsers;
    };



    static CheckedFunction<ResultSet,List<RegisterRequestModel>> getRegisteredUsersModel= resultSet -> {
        List<RegisterRequestModel> registeredUsers = new ArrayList<>();
        try {
            while (resultSet.next()) {
                String email = resultSet.getString("userLogin");
                String password = resultSet.getString("userPassword");
                RegisterRequestModel registerRequestModel = new RegisterRequestModel(email, password);
                registeredUsers.add(registerRequestModel);
            }
        }
        catch (SQLException e){
            throw e;
        }
        return registeredUsers;
    };



    //TODO DELETE AFTER CREATION. It's just an example
    static Function<ResultSet,List<String>> ProcessUsers  = arg -> {
        List<String> usersLogins = new ArrayList<>();
        try {
            while (arg.next()) {
                usersLogins.add(arg.getString("userLogin"));
            }
        }
        catch (Exception e){
            usersLogins =null;
        }
        return usersLogins;
    };

}
