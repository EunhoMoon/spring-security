package me.janek.securityjava.common;

import java.util.UUID;

public class TokenGenerator {

    public static String generateToken(String prefix) {
        return prefix + UUID.randomUUID();
    }

}
