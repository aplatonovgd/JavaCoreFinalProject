package com.litmos.gridu.javacore.aplatonov.BusinessLogic.Processors.RequestAndOther;

import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Exceptions.InvalidEmailException;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Exceptions.InvalidJsonException;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Exceptions.InvalidPasswordException;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Exceptions.UserExistException;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Helpers.RequestHelper;
import com.litmos.gridu.javacore.aplatonov.Database.DBProcessor;
import com.litmos.gridu.javacore.aplatonov.Models.RegisterRequestModel;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class RegistrationRequestProcessor extends AbstractPostRequestProcessor {

    protected boolean hashPassword;

    public RegistrationRequestProcessor(HttpServletRequest request, DBProcessor dbProcessor, boolean hashPassword) throws IOException {
        super(request, dbProcessor);
        this.hashPassword = hashPassword;
    }


    public void processRequest() throws InvalidJsonException, SQLException, UserExistException, NoSuchAlgorithmException, InvalidEmailException, InvalidPasswordException {

        RegisterRequestModel registerRequest = parseJson(requestBody);
        checkUserEmail(registerRequest.getEmail());
        checkUserPassword(registerRequest.getPassword());

        List<RegisterRequestModel> registeredUsers = dbProcessor.getRegisterRequestModelListByLogin(registerRequest.getEmail());


        final String emailToCheck =registerRequest.getEmail();
        Optional<RegisterRequestModel> checkUser = registeredUsers.stream().
                filter(p -> p.getEmail().equals(emailToCheck)).
                findAny();

        if (checkUser.isPresent()){
            throw new UserExistException("User already exist");
        }
        else {

            if (hashPassword){
                String password = RequestHelper.calculatePasswordHash(registerRequest.getPassword());
                registerRequest = new RegisterRequestModel(registerRequest.getEmail(),password);
            }

            dbProcessor.addUserToDB(registerRequest);

        }

    }

   @Override
   protected RegisterRequestModel parseJson(String json) throws InvalidJsonException {

      RegisterRequestModel registerRequest;

      try{
          registerRequest =  gson.fromJson(json, RegisterRequestModel.class);
          if (registerRequest == null ||  registerRequest.getEmail() == null || registerRequest.getPassword() == null){
              throw new InvalidJsonException("Invalid JSON");
          }
      }
      catch (Exception e ){
        throw  new InvalidJsonException(e.getMessage());
      }

      return registerRequest;
    }


}
