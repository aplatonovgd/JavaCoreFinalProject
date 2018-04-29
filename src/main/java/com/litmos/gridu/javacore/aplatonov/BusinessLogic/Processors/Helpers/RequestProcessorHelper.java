package com.litmos.gridu.javacore.aplatonov.BusinessLogic.Processors.Helpers;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class RequestProcessorHelper {

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

}
