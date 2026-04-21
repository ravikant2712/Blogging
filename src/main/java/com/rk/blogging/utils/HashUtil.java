package com.rk.blogging.utils;

import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.util.Base64;

@Component
public class HashUtil {
    public String hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(input.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException("Hashing failed");
        }
    }
}
