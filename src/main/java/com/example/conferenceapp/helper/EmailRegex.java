package com.example.conferenceapp.helper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailRegex {

    static final String regex = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
    static final Pattern pattern = Pattern.compile(regex);

    public static boolean matchEmail(String email) {
        Matcher matcher = pattern.matcher(email);
        return !matcher.matches();
    }
}
