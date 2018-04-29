package com.litmos.gridu.javacore.aplatonov.Database;

import com.litmos.gridu.javacore.aplatonov.models.LoginRequestModel;
import com.litmos.gridu.javacore.aplatonov.models.RegisterRequestModel;

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


    public void addUserToDB(RegisterRequestModel registerRequestModel) throws SQLException {

        insertStatementHandler("INSERT INTO `users` (`userLogin`, `userPassword`, `isBlocked`) VALUES (?,?,?)",
                registerRequestModel.getEmail(), registerRequestModel.getPassword(),0);
    }


    public List<LoginRequestModel> getLoginRequestModleListByLogin(String email) throws SQLException, IllegalArgumentException {
        List<LoginRequestModel> userList;
        userList = selectStatementHandler(getLoginUserModel,"select * from users where userLogin = ?", email);
        return userList;
    }

    public List<RegisterRequestModel> getRegisterRequestModelListByLogin(String email) throws SQLException, IllegalArgumentException {
        List<RegisterRequestModel> userList;
        userList = selectStatementHandler(getRegisteredUsersModel,"select * from users where userLogin = ?", email);
        return userList;
    }



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


  /*  public List<LoggedinUser> getUsers() throws SQLException {

        List<LoggedinUser> userList= new ArrayList<>();

        try (Connection connection = getDataBaseConnection(false)){

            Statement usersStatement = connection.createStatement();
            String sqlSelectQuery = "select * from Users";

           ResultSet resultSet =  usersStatement.executeQuery(sqlSelectQuery);

            String userId;
            String userPasswordHash;
            String userEmail;
            int isBlocked;
            while (resultSet.next()){
               userId = resultSet.getString("userId");
               userEmail = resultSet.getString("userLogin");
               userPasswordHash = resultSet.getString("userPassword");
               isBlocked = resultSet.getInt("isBlocked");
               userList.add(new LoggedinUser(userId,userEmail,userPasswordHash,isBlocked));
           }
        }
        return userList;
    }*/

    /*public List<Products> getProducts (){

        try(Connection sqlConnection = getDataBaseConnection(true)) {

           // Statement statement = sqlConnection.createStatement();
           // Statement dataBaseCreationStatement = generateBatch(dbScript, sqlConnection, statement);
            dataBaseCreationStatement.executeBatch();
            dataBaseCreationStatement.close();

        }

    }*/