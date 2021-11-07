package me.dwamuya.fakeddit.security;

import org.springframework.beans.factory.annotation.Value;

public class SecurityConstants {

    @Value("${jwt.token.secret}")
    public static String SECRET_KEY = "bXlmdWNraW5nYmFzZTY0ZW5jb2RlZHNlY3auTyJldGtleQ==";

}