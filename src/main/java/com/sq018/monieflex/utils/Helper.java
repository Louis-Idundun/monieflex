package com.sq018.monieflex.utils;

import com.sq018.monieflex.exceptions.MonieFlexException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Helper {
    public static String getTokenFromLink(String link, String prefix) {
        Pattern pattern = Pattern.compile(prefix + "verify=([a-zA-Z0-9]+)");
        Matcher matcher = pattern.matcher(link);

        if (matcher.find()) {
            return matcher.group(1);
        } else {
            throw new MonieFlexException("Link is not properly formatted");
        }
    }
}
