package com.litmos.gridu.javacore.aplatonov.BusinessLogic.Validators;

import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Exceptions.SessionNotFoundException;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Helpers.RequestHelper;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Objects.LoggedinUser;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Processors.Request.LoginRequestProcessor;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Objects.ValidationResult;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

public abstract class AbstractSecureRequestValidator extends AbstractRequestValidator {

    protected List<Cookie> cookies;
    protected LoginRequestProcessor.LoggedInUserInfo loggedInUserInfo;
    boolean isUnauthorizedRequestExpected;
    protected ServletContext servletContext;

    public AbstractSecureRequestValidator(HttpServletRequest request, LoginRequestProcessor.LoggedInUserInfo loggedInUserInfo,
                                          boolean isUnauthorizedRequestExpected, ServletContext servletContext){
        super(request);
        this.loggedInUserInfo = loggedInUserInfo;
        if(request.getCookies() != null) {
            this.cookies = RequestHelper.getCookiesList(request.getCookies());
        }
        this.isUnauthorizedRequestExpected = isUnauthorizedRequestExpected;
        this.servletContext = servletContext;

    }


    @Override
    public ValidationResult getRequestValidationResult(){
        ValidationResult validationResultModel;

        validationResultModel = getHttpHeadersValidationResult(request);

        if (validationResultModel.isSuccess()) {
            servletContext.log("Servlet request method is correct. Go go to cookies validation");
          return getCookiesValidationResult();
        }

        return validationResultModel;
    }


    protected ValidationResult getCookiesValidationResult(){

        if (isUnauthorizedRequestExpected){
            servletContext.log("Validate cookies for unauthorized request started");
           return getCookiesValidationResultForUnauthorizedUser();
        }
        else {
            servletContext.log("Validate cookies for authorized request started");
            return getCookiesValidationResultForAuthorizedUser();
        }

    }

    protected ValidationResult getCookiesValidationResultForAuthorizedUser() {
       try {
           CheckSessionIdAndPasswordHash();
        }
        catch (SessionNotFoundException e ) {
            servletContext.log(e.getMessage());
            return new ValidationResult(false,"Unauthorized", "User is not authorized");
        }

       return new ValidationResult(true);
    }

    protected ValidationResult getCookiesValidationResultForUnauthorizedUser() {
        try {
            CheckSessionIdAndPasswordHash();
        }
        catch (SessionNotFoundException e ) {
            servletContext.log(e.getMessage());
            return new ValidationResult(true);
        }

        return new ValidationResult(false, "UserAlreadyLoggedIn","Log out to do this operation");
    }

    protected void CheckSessionIdAndPasswordHash() throws SessionNotFoundException {
        String sessionId = RequestHelper.getRequestSessionId(cookies);
        String passHash = RequestHelper.getRequestPasswordHash(cookies);
        findUserInLoggedInUserInfo(sessionId,passHash);
    }


    //TODO REMOVE
   /* protected String getRequestSessionId() throws SessionNotFoundException {

        if(cookies == null) {
            throw new SessionNotFoundException("SessionId not found in cookies list");
        }
        Optional<Cookie> sessionCookie = cookies.stream()
                .filter(x -> x.getName().equals("sessionId"))
                .findFirst();
        if(!sessionCookie.isPresent()){
            throw new SessionNotFoundException("SessionId not found in cookies list");
        }

        return sessionCookie.get().getValue();
    }

    protected String getRequestPasswordHash() throws SessionNotFoundException {

        if(cookies == null) {
            throw new SessionNotFoundException("SessionId not found in cookies list");
        }

        Optional<Cookie> passHashCookie = cookies.stream()
                .filter(x -> x.getName().equals("ph"))
                .findFirst();
        if(!passHashCookie.isPresent()){
            throw new SessionNotFoundException("PasswordHash not found in cookies list");
        }
        return passHashCookie.get().getValue();
    }*/

    protected void findUserInLoggedInUserInfo (String sessionId, String passHash) throws SessionNotFoundException {

        LoggedinUser user = loggedInUserInfo.getUserBySessionId(sessionId);

        if(!passHash.equals(user.getUserPasswordHash())){
            throw new SessionNotFoundException("Password hash in cookies is not equal to server hash");
        }
    }


}
