package com.litmos.gridu.javacore.aplatonov.BusinessLogic.Processors.Request;

import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Objects.IncorrectNameOrPasswordException;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Objects.InvalidJsonException;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Objects.LoggedinUser;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Objects.SessionNotFoundException;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Processors.Helpers.RequestProcessorHelper;
import com.litmos.gridu.javacore.aplatonov.Database.DBProcessor;
import com.litmos.gridu.javacore.aplatonov.models.LoginRequestModel;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.*;

public class LoginRequestProcessor extends AbstractRequestProcessor {

private boolean hashPassword;

private LoggedInUserInfo loggedInUserInfo;

    public LoginRequestProcessor(HttpServletRequest request, DBProcessor dbProcessor, boolean hashPassword, LoggedInUserInfo loggedInUserInfo) throws IOException {
        super(request, dbProcessor);
        this.hashPassword = hashPassword;
        this.loggedInUserInfo = loggedInUserInfo;
    }


    public String processUser() throws SQLException, InvalidJsonException, IncorrectNameOrPasswordException, NoSuchAlgorithmException {

        LoginRequestModel loginRequest = parseJson(jsonResponseBody);
        List<LoginRequestModel> registeredUsers = dbProcessor.getLoginRequestModleListByLogin(loginRequest.getEmail());

        Optional<LoginRequestModel> databaseUser = checkLoginInDatabase(loginRequest.getEmail(), registeredUsers);
        checkPassword(hashPassword,loginRequest.getPassword(), databaseUser.get().getPassword());

        String sessionId = RequestProcessorHelper.generateSessionId();
        long sessionCreatedTime = RequestProcessorHelper.getCreationTime();
        LoggedinUser loggedinUser = new LoggedinUser(databaseUser.get().getUserId(),databaseUser.get().getEmail(),databaseUser.get().getPassword(),sessionCreatedTime);
        loggedInUserInfo.addLoginInfo(sessionId, loggedinUser);

        return sessionId;
    }


    private void checkPassword(boolean hashPassword, String loginRrequestPassword, String databasePassword) throws NoSuchAlgorithmException, IncorrectNameOrPasswordException {

        if (hashPassword) {
            String cleanPass = loginRrequestPassword;
            loginRrequestPassword = RequestProcessorHelper.calculatePasswordHash(cleanPass);
        }

        if (!loginRrequestPassword.equals(databasePassword)) {
            throw new IncorrectNameOrPasswordException("Incorrect login or password");
        }
    }

    private  Optional<LoginRequestModel> checkLoginInDatabase(String email, List<LoginRequestModel> registeredUsers) throws IncorrectNameOrPasswordException {
        //final String emailToCheck = email;
        Optional<LoginRequestModel> dataBaseUser = registeredUsers.stream().
                filter(p -> p.getEmail().equals(email)).
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

        private void addLoginInfo(String sessionId, LoggedinUser loggedinUser) {

            sessionIdToUserMap.put(sessionId, loggedinUser);
        }


    }



}

