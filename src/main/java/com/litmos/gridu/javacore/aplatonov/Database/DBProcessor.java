package com.litmos.gridu.javacore.aplatonov.Database;

import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Objects.LoggedinUser;
import com.litmos.gridu.javacore.aplatonov.Models.ItemModel;
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


    public void updateProductsTable(List<ItemModel> itemModels) throws IOException, SQLException {
        executeProductsTableUpdate("update products set productQuantity = productQuantity - ? where productId = ?",itemModels);
    }

    public void executeProductsTableUpdate(String sql, List<ItemModel> itemList) throws IOException, SQLException {
        try(Connection sqlConnection = getDataBaseConnection()) {
            PreparedStatement statement = sqlConnection.prepareStatement(sql);
            for (ItemModel item : itemList) {
               statement = addBatchToPreparedStatementsHandler(statement, item.getProductId(),item.getQuantity());
            }
            statement.executeBatch();
            statement.close();
        }
    }

    public void insertOrderIntoOrdersTable(String sql, List<ItemModel> itemList, String orderId, int userId) throws IOException, SQLException {

        try(Connection sqlConnection = getDataBaseConnection()) {
            PreparedStatement statement = sqlConnection.prepareStatement(sql);
            for (ItemModel item : itemList) {
                statement = addBatchToPreparedStatementsHandler(statement, orderId, userId, item.getProductId(), item.getQuantity());
            }
            statement.executeBatch();
            statement.close();
        }
    }

    public void createOrder(List<ItemModel> itemList, String orderId, String userId) throws IOException, SQLException {

        insertOrderIntoOrdersTable("INSERT INTO `orders` (`orderId`,`userId`,`productId`,`productQuantity`)VALUES(?,?,?,?)",
                itemList, orderId, Integer.valueOf(userId));
    }


    public List<ItemModel> getItemListFromDatabseById(int productId) throws SQLException {
        List<ItemModel> itemModelList = selectStatementHandler(getItem, "select * from products where productId = ?", productId);
        return itemModelList;
    }


    public void addUserToDB(RegisterRequestModel registerRequestModel) throws SQLException {

        insertAndUpdateStatementsHandler("INSERT INTO `users` (`userLogin`, `userPassword`, `isBlocked`) VALUES (?,?,?)",
                registerRequestModel.getEmail(), registerRequestModel.getPassword(),0);
    }


    public List<ProductModel> getProducts() throws SQLException {
         List<ProductModel> productModels;
         productModels = selectStatementHandler(getProducts,"select * from products");
         return productModels;
    }

    public List<LoggedinUser> getLoginRequestModelListByLogin(String email) throws SQLException, IllegalArgumentException {
        List<LoggedinUser> userList;
        userList = selectStatementHandler(getLoginUserModel,"select * from users where userLogin = ?", email);
        return userList;
    }

    public List<RegisterRequestModel> getRegisterRequestModelListByLogin(String email) throws SQLException, IllegalArgumentException {
        List<RegisterRequestModel> userList;
        userList = selectStatementHandler(getRegisteredUsersModel,"select * from users where userLogin = ?", email);
        return userList;
    }




    static CheckedFunction<ResultSet,List<ItemModel>> getItem = resultSet -> {
        List<ItemModel> itemModelList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                String productId = String.valueOf(resultSet.getInt("productId"));
                String title = resultSet.getString("productTitle");
                String quantity = resultSet.getString("productQuantity");
                String productPrice = resultSet.getString("productPrice");
                ItemModel itemModel = new ItemModel(productId,title,quantity,productPrice);
                itemModelList.add(itemModel);
            }
        }
        catch (SQLException e){
            throw  e;
        }
        return itemModelList;
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


    static CheckedFunction<ResultSet,List<LoggedinUser>> getLoginUserModel= resultSet -> {
        List<LoggedinUser> registeredUsers = new ArrayList<>();
        try {
            while (resultSet.next()) {
                String email = resultSet.getString("userLogin");
                String password = resultSet.getString("userPassword");
                int userId = resultSet.getInt("userId");
                int isBlocked = resultSet.getInt("isBlocked");
               LoggedinUser loginRequestModel = new LoggedinUser(userId, email, password, isBlocked);
               registeredUsers.add(loginRequestModel);
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
