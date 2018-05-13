package com.litmos.gridu.javacore.aplatonov.Database;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.List;
import java.util.stream.Collectors;

public class DBConnector {

    private String fullDbUrl;
    private String cleanDbUrl;
    protected String dbName;
    private String dbUsername;
    private String dbPassword;

    private Connection dbConnection;

    protected void initDBConnector(String dbUrl, String dbName, String dbUsername, String dbPassword){
            this.dbName = dbName;
            this.cleanDbUrl = dbUrl;
            this.fullDbUrl = dbUrl + dbName;
            this.dbUsername = dbUsername;
            this.dbPassword = dbPassword;
    }

    protected Connection getDataBaseConnection() throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
         try {
             Connection dbConnection = DriverManager.getConnection(fullDbUrl,dbUsername,dbPassword);
             this.dbConnection = dbConnection;
         }
         catch (SQLException e){
             dbConnection = null;
            throw e;
        }
        return dbConnection;
    }

    protected Connection getSQLServerOnlyConnection() throws SQLException {
        try {
            Connection dbConnection = DriverManager.getConnection(cleanDbUrl,dbUsername,dbPassword);
            this.dbConnection = dbConnection;
        }
        catch (SQLException e){
            dbConnection = null;
            throw e;
        }
        return dbConnection;
    }


    public Statement generateBatch(List<String> sqlScript, Connection connection, Statement statement) throws SQLException {
        for(String line : sqlScript){
            statement.addBatch(line);
        }
        return statement;
    }

    public void executeBatchStatementOnServer(String sqlCreateScript) throws IOException, SQLException {
        List<String> dbScript = parseSQLScript(sqlCreateScript);
        try(Connection sqlConnection = getSQLServerOnlyConnection()) {
            Statement statement = sqlConnection.createStatement();
            Statement dataBaseCreationStatement = generateBatch(dbScript, sqlConnection, statement);
            dataBaseCreationStatement.executeBatch();
            dataBaseCreationStatement.close();
        }
    }

    public List<String> parseSQLScript(String filepath) throws IOException {
        List<String> dbScript  = Files.readAllLines(Paths.get(filepath), Charset.defaultCharset());
        dbScript = dbScript.stream().map(r -> r.replaceAll("dbname",dbName)).collect(Collectors.toList());//.forEach(arg -> arg.replaceAll("dbname",dbName));
        return dbScript;
    }




    protected <T> PreparedStatement addBatchToPreparedStatementsHandler(PreparedStatement preparedStatement , T... parameters) throws SQLException, IllegalArgumentException {
        if (parameters.length >0) {
            AddParamsToStatement(preparedStatement, parameters);
        }
        preparedStatement.addBatch();
        return preparedStatement;
    }



    /**
     * @param function Resultset processor. Use it to process data from result Set.
     * @param sqlQuery  parametrized sql query
     * @param parameters  parameters for sql query
     * @param <T> Type of parameters for sql query. NB: ONLY STRING AND INT PARAMS ARE SUPPORTED
     * @param <R> Type of returned list of params.
     * @return
     * @throws SQLException
     * @throws IllegalArgumentException
     */
    protected <T,R> List<R> selectStatementHandler(CheckedFunction<ResultSet,List<R>> function, String sqlQuery, T... parameters) throws SQLException, IllegalArgumentException {

        List<R> arrayList;
        ResultSet rs;
        try(Connection connection = getDataBaseConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {

            if (parameters.length > 0) {
                AddParamsToStatement(preparedStatement, parameters);
            }
            rs =  preparedStatement.executeQuery();
            arrayList = function.apply(rs);
        }
        return arrayList;
    }



    protected <T> void insertAndUpdateStatementsHandler(String sqlQuery, T... parameters) throws SQLException, IllegalArgumentException {
        try(Connection connection = getDataBaseConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            if (parameters.length >0) {
                AddParamsToStatement(preparedStatement, parameters);
            }
            preparedStatement.execute();
        }
    }

    /**
     * Adds paramters to the PreparedStatement
     * @param statement PreparedStatement
     * @param parameters Parameters to add
     * @param <T> - Type of parameters
     * @return
     * @throws SQLException
     * @throws IllegalArgumentException is thrown if type of SQL parameter is not supported
     */
    protected  <T> PreparedStatement AddParamsToStatement(PreparedStatement statement, T... parameters) throws SQLException, IllegalArgumentException {

        for (int i =0; i < parameters.length; i++){
            String typeName = parameters[i].getClass().getSimpleName();

            switch (typeName) {
                case "Integer":
                    Integer varInt = (Integer) parameters[i];
                    statement.setInt(i+1,varInt);
                    break;
                case "String":
                    String varStr = (String) parameters[i];
                    statement.setString(i+1,varStr);
                    break;
                default:
                    throw new IllegalArgumentException(typeName.getClass().getSimpleName()+ "is not supported");
            }
        }

        return statement;
    }



}
