package org.parsejava.restexecutors;


import org.json.JSONObject;

import java.util.HashMap;

/**
 * @author mrisvanv
 * @since 2020-04-22
 */
public class Utils {

    static JSONObject exceptionToCode(Exception ex) {
        String errorMsg;
        int errorCode;
        if (ex.getMessage().contains("java.net.UnknownHostException:")) {
            errorMsg = "Failed to connect to server";
            errorCode = 408;
        } else {
            errorCode = 400;
            errorMsg = "Failed to fetch data from server";
        }
        HashMap<String, Object> error = new HashMap<>();
        error.put("error", errorMsg);
        error.put("code", errorCode);
        return new JSONObject(error);
    }
}