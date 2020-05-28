package org.parsejava.restexecutors;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequestWithBody;
import org.json.JSONException;
import org.json.JSONObject;
import org.parsejava.Parse;
import org.parsejava.ParseUser;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.parsejava.Parse.MASTER_ID;
import static org.parsejava.Parse.PARSE_LOGGER;


/**
 * @author mrisvanv
 * @since 2020-04-26
 */
public class ObjectRelated {

    static final Logger LOGGER = PARSE_LOGGER;
    private static final String CLASS_PATH = Parse.SERVER_URL + "/classes/";

    public static JSONObject restObjectPut(JSONObject requestData, String className, String objectId) {
        HashMap<String, Object> result = new HashMap<>();
        try {
            HttpRequestWithBody request = Unirest.put(CLASS_PATH + className + "/" + objectId);
            request.header(Constants.HEADER_APP_ID, Parse.APP_ID)
                    .header(Constants.HEADER_CLIENT_KEY, Parse.CLIENT_ID);
            if (Parse.MASTER_ID != null && !Parse.MASTER_ID.equals("")) {
                request.header(Constants.HEADER_MASTER_KEY, MASTER_ID);
            }
            String sessionId = ParseUser.getCurrentUser().getSessionId();
            if (sessionId != null && !sessionId.trim().equals("")) {
                request.header(Constants.HEADER_SESSION_TOKEN, sessionId);
            }
            request.body(requestData.toString());
            HttpResponse<String> response = request.asString();
            if (response.getStatus() == 200) {
                result.put("d", new JSONObject(response.getBody()));
            } else {
                result.put("e", new JSONObject(response.getBody()));
            }
        } catch (UnirestException e) {
            JSONObject errorCode = Utils.exceptionToCode(e);
            result.put("e", errorCode);
            if (errorCode.optInt("code") == 0) {
                LOGGER.log(Level.WARNING, "Server communication failed", e);
            }
        } catch (JSONException e) {
            JSONObject errorCode = Utils.exceptionToCode(e);
            result.put("e", errorCode);
            LOGGER.log(Level.WARNING, "Converting to json failed", e);
        }
        return new JSONObject(result);
    }

    public static JSONObject restObjectPost(JSONObject requestData, String className) {
        HashMap<String, Object> result = new HashMap<>();
        try {
            HttpRequestWithBody request = Unirest.post(CLASS_PATH + className);
            request.header(Constants.HEADER_APP_ID, Parse.APP_ID)
                    .header(Constants.HEADER_CLIENT_KEY, Parse.CLIENT_ID);
            String sessionId = ParseUser.getCurrentUser().getSessionId();
            if (sessionId != null && !sessionId.trim().equals("")) {
                request.header(Constants.HEADER_SESSION_TOKEN, sessionId);
            }
            request.body(requestData.toString());
            HttpResponse<String> response = request.asString();
            if (response.getStatus() == 201) {
                result.put("d", new JSONObject(response.getBody()));
            } else {
                result.put("e", new JSONObject(response.getBody()));
            }
        } catch (UnirestException e) {
            JSONObject errorCode = Utils.exceptionToCode(e);
            result.put("e", errorCode);
            if (errorCode.optInt("code") == 0) {
                LOGGER.log(Level.WARNING, "Server communication failed", e);
            }
        } catch (JSONException e) {
            JSONObject errorCode = Utils.exceptionToCode(e);
            result.put("e", errorCode);
            LOGGER.log(Level.WARNING, "Converting to json failed", e);
        }
        return new JSONObject(result);
    }


    public static JSONObject restObjectDelete(String className, String objectId) {
        HashMap<String, Object> result = new HashMap<>();
        try {
            HttpRequestWithBody request = Unirest.delete(CLASS_PATH + className + "/" + objectId);
            request.header(Constants.HEADER_APP_ID, Parse.APP_ID)
                    .header(Constants.HEADER_CLIENT_KEY, Parse.CLIENT_ID);
            String sessionId = ParseUser.getCurrentUser().getSessionId();
            if (sessionId != null && !sessionId.trim().equals("")) {
                request.header(Constants.HEADER_SESSION_TOKEN, sessionId);
            }
            HttpResponse<String> response = request.asString();
            if (response.getStatus() == 200) {
                result.put("d", new JSONObject(response.getBody()));
            } else {
                result.put("e", new JSONObject(response.getBody()));
            }
        } catch (UnirestException e) {
            JSONObject errorCode = Utils.exceptionToCode(e);
            result.put("e", errorCode);
            if (errorCode.optInt("code") == 0) {
                LOGGER.log(Level.WARNING, "Server communication failed", e);
            }
        } catch (JSONException e) {
            JSONObject errorCode = Utils.exceptionToCode(e);
            result.put("e", errorCode);
            LOGGER.log(Level.WARNING, "Converting to json failed", e);
        }
        return new JSONObject(result);
    }
}
