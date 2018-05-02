package com.litmos.gridu.javacore.aplatonov.BusinessLogic.Processors.RequestAndOther;

import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Exceptions.*;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Helpers.RequestHelper;
import com.litmos.gridu.javacore.aplatonov.Database.DBProcessor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

public class LogoutRequestProccessor extends LoginRequestProcessor {


    public LogoutRequestProccessor(HttpServletRequest request, DBProcessor dbProcessor, LoggedInUserInfo loggedInUserInfo) throws IOException {
        super(request, dbProcessor, loggedInUserInfo);
    }

    @Override
    public String processRequest() throws SessionNotFoundException {
        List<Cookie> coookieList = RequestHelper.getCookiesList(request.getCookies());
        String sessionId =RequestHelper.getRequestSessionId(coookieList);
        loggedInUserInfo.removeUserBySessionId(sessionId);

        return "";
    }
}
