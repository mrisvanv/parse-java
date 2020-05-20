package org.parsejava.restexecutors;


import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONException;
import org.json.JSONObject;
import org.parsejava.Parse;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.parsejava.Parse.PARSE_LOGGER;


/**
 * @author mrisvanv
 * @since 2020-04-17
 */
public class UserRelated {
    static final Logger LOGGER = PARSE_LOGGER;
    private static final String LOGIN_PATH = Parse.SERVER_URL + "/login";
    private static final String LOGOUT_PATH = Parse.SERVER_URL + "/logout";
    private static final String USER_ME_PATH = Parse.SERVER_URL + "/users/me";

    public static JSONObject doLogin(String username, String password) {
        HttpResponse<String> response;
        HashMap<String, Object> results = new HashMap<>();
        try {
            response = Unirest.get(LOGIN_PATH)
                    .header(Constants.HEADER_APP_ID, Parse.APP_ID)
                    .header(Constants.HEADER_CLIENT_KEY, Parse.CLIENT_ID)
                    .header(Constants.HEADER_REVOCABLE_SESSION, "1")
                    .queryString("username", username)
                    .queryString("password", password)
                    .asString();
            JSONObject result = new JSONObject(response.getBody());
            if (result.has("sessionToken")) {
                return result.put("status", "success");

            } else {
                return result.put("status", "failed");
            }
        } catch (UnirestException e) {
            JSONObject errorCode = Utils.exceptionToCode(e);
            results.put("e", errorCode);
            if (errorCode.optInt("code") == 0) {
                LOGGER.log(Level.WARNING, "Server communication failed", e);
            }
            return new JSONObject(results);
        } catch (JSONException e) {
            LOGGER.log(Level.WARNING, "User login failed", e);
            return new JSONObject();
        }
    }

    public static JSONObject doLogin(String sessionId) {
        HttpResponse<String> response;
        try {
            response = Unirest.get(USER_ME_PATH)
                    .header(Constants.HEADER_APP_ID, Parse.APP_ID)
                    .header(Constants.HEADER_CLIENT_KEY, Parse.CLIENT_ID)
                    .header(Constants.HEADER_SESSION_TOKEN, sessionId)
                    .asString();
            JSONObject result = new JSONObject(response.getBody());
            if (result.has("sessionToken")) {
                return result.put("status", "success");

            } else {
                return result.put("status", "failed");
            }
        } catch (UnirestException | JSONException e) {
            if (e.getMessage().contains("java.net.UnknownHostException:")) {
                LOGGER.warning("Failed to connect to server");
            } else {
                LOGGER.log(Level.WARNING, "User login failed", e);
            }
            return new JSONObject();
        }
    }

    public static JSONObject doLogout(String sessionId) {
        HttpResponse<String> response;
        try {
            response = Unirest.post(LOGOUT_PATH)
                    .header(Constants.HEADER_APP_ID, Parse.APP_ID)
                    .header(Constants.HEADER_CLIENT_KEY, Parse.CLIENT_ID)
                    .header(Constants.HEADER_SESSION_TOKEN, sessionId)
                    .asString();
            JSONObject result = new JSONObject(response.getBody());
            if (result.length() == 0) {
                return result.put("status", "success");

            } else {
                return result.put("status", "failed");
            }
        } catch (UnirestException | JSONException e) {
            LOGGER.log(Level.WARNING, "User login failed", e);
            return new JSONObject();
        }
    }
}
