package com.litmos.gridu.javacore.aplatonov.BusinessLogic.Helpers;

import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Exceptions.SessionNotFoundException;

import javax.servlet.http.Cookie;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class RequestHelper {

    public static String calculatePasswordHash(String password) throws NoSuchAlgorithmException {

       MessageDigest digest = MessageDigest.getInstance("SHA-256");
       byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
       byte[] encoded = Base64.getEncoder().encode(hash);

        return new String(encoded);
    }

    public static String generateSessionId (){
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    public static long getCreationTime(){

        return System.currentTimeMillis();
    }

    public static List<Cookie> getCookiesList(Cookie [] cookies){
        List<Cookie> cookieList = Arrays.asList(cookies);
        return cookieList;
    }

    public static String getRequestSessionId(List<Cookie> cookies) throws SessionNotFoundException {

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

    public static String getRequestPasswordHash(List<Cookie> cookies) throws SessionNotFoundException {

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
    }

}
