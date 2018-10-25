package com.mmohaule.router.controller;

import com.mmohaule.router.fixmsg.*;

public class ResponseGenerator {

    public static String extractTargetCompId(String request) {
        int         startIndex;
        int         endIndex;
        String      senderCompIdTagVal;
        String[]    tokens;

        startIndex = request.indexOf(FixMsg.TARGET_COMP_ID_TAG + FixMsg.TAG_VAL_LINK);
        endIndex = request.indexOf(FixMsg.TAG_VAL_SEPARATOR, startIndex);
        senderCompIdTagVal = request.substring(startIndex, endIndex);
        tokens = senderCompIdTagVal.split(FixMsg.TAG_VAL_LINK);
        return (tokens[1]);
    }
    
    public static String extractCompId(String request) {
        String[]    tokens;
        
        tokens = request.split(" ");
        int len = tokens.length;
        return (tokens[len - 1]);
    }
}
