package com.litmos.gridu.javacore.aplatonov.BusinessLogic.Processors.Request;

import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Exceptions.IncorrectNameOrPasswordException;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Exceptions.InvalidJsonException;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Objects.LoggedinUser;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Exceptions.SessionNotFoundException;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Helpers.RequestHelper;
import com.litmos.gridu.javacore.aplatonov.Database.DBProcessor;
import com.litmos.gridu.javacore.aplatonov.Models.LoginRequestModel;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.*;

public class LoginRequestProcessor extends AbstractPostRequestProcessor {

private boolean hashPassword;

private LoggedInUserInfo loggedInUserInfo;

    public LoginRequestProcessor(HttpServletRequest request, DBProcessor dbProcessor, boolean hashPassword, LoggedInUserInfo loggedInUserInfo) throws IOException {
        super(request, dbProcessor);
        this.hashPassword = hashPassword;
        this.loggedInUserInfo = loggedInUserInfo;
    }


    public String processRequest() throws SQLException, InvalidJsonException, IncorrectNameOrPasswordException, NoSuchAlgorithmException {

        LoginRequestModel loginRequest = parseJson(requestBody);
        List<LoggedinUser> registeredUsers = dbProcessor.getLoginRequestModelListByLogin(loginRequest.getEmail());

        Optional<LoggedinUser> databaseUser = checkLoginInDatabase(loginRequest.getEmail(), registeredUsers);
        checkPassword(hashPassword,loginRequest.getPassword(), databaseUser.get().getUserPasswordHash());

        String sessionId = RequestHelper.generateSessionId();
        long sessionCreatedTime = RequestHelper.getCreationTime();

        LoggedinUser loggedinUser = new LoggedinUser(databaseUser.get().getUserId(),databaseUser.get().getUserEmail(),databaseUser.get().getUserPasswordHash(),sessionCreatedTime);
        loggedInUserInfo.addLoginInfo(sessionId, loggedinUser);

        return sessionId;
    }


    private void checkPassword(boolean hashPassword, String loginRrequestPassword, String databasePassword) throws NoSuchAlgorithmException, IncorrectNameOrPasswordException {

        if (hashPassword) {
            String cleanPass = loginRrequestPassword;
            loginRrequestPassword = RequestHelper.calculatePasswordHash(cleanPass);
        }

        if (!loginRrequestPassword.equals(databasePassword)) {
            throw new IncorrectNameOrPasswordException("Incorrect login or password");
        }
    }

    private  Optional<LoggedinUser> checkLoginInDatabase(String email, List<LoggedinUser> registeredUsers) throws IncorrectNameOrPasswordException {
        //final String emailToCheck = email;
        Optional<LoggedinUser> dataBaseUser = registeredUsers.stream().
                filter(p -> p.getUserEmail().equals(email)).
                findFirst();

        if (!dataBaseUser.isPresent()){
            throw new IncorrectNameOrPasswordException("Incorrect login or password");
        }
        else
            return dataBaseUser;
    }


    @Override
    protected LoginRequestModel parseJson(String json) throws InvalidJsonException {

        LoginRequestModel loginRequest =  gson.fromJson(json, LoginRequestModel.class);

        if (loginRequest == null ||  loginRequest.getEmail() == null || loginRequest.getPassword() == null){
            throw new InvalidJsonException("Invalid JSON");
        }

        return loginRequest;
    }


    public static class LoggedInUserInfo {

        private Map<String,LoggedinUser> sessionIdToUserMap= new HashMap<>();

        public LoggedinUser getUserBySessionId(String sessionId) throws SessionNotFoundException {
            LoggedinUser loggedinUser;
            if (sessionIdToUserMap.containsKey(sessionId)) {
                loggedinUser = sessionIdToUserMap.get(sessionId);
            }
            else {
                throw new SessionNotFoundException("Session not found");
            }
            return loggedinUser;
        }

        public int getUserIdBySessionId(String sessionId) throws SessionNotFoundException {
            int userId;
            if (sessionIdToUserMap.containsKey(sessionId)) {
                LoggedinUser loggedinUser = sessionIdToUserMap.get(sessionId);
                userId = loggedinUser.getUserId();
            }
            else {
                throw new SessionNotFoundException("Session not found");
            }

            return userId;
        }


        private void addLoginInfo(String sessionId, LoggedinUser loggedinUser) {

            sessionIdToUserMap.put(sessionId, loggedinUser);
        }


    }



}

