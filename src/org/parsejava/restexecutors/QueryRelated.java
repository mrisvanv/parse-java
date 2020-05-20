package org.parsejava.restexecutors;


import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequest;
import org.json.JSONException;
import org.json.JSONObject;
import org.parsejava.Parse;
import org.parsejava.ParseUser;

import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.parsejava.Parse.PARSE_LOGGER;


/**
 * @author mrisvanv
 * @since 2020-04-24
 */
public class QueryRelated {

    static final Logger LOGGER = PARSE_LOGGER;
    private static final String CLASS_PATH = Parse.SERVER_URL + "/classes/";

    public static JSONObject restGet(JSONObject requestData, String className) {
        HashMap<String, Object> result = new HashMap<>();
        try {
            HttpRequest request = Unirest.get(CLASS_PATH + className);
            request.header(Constants.HEADER_APP_ID, Parse.APP_ID)
                    .header(Constants.HEADER_CLIENT_KEY, Parse.CLIENT_ID);
            String sessionId = ParseUser.getCurrentUser().getSessionId();
            if (sessionId != null && !sessionId.trim().equals("")) {
                request.header(Constants.HEADER_SESSION_TOKEN, sessionId);
            }
            for (Iterator it = requestData.keys(); it.hasNext(); ) {
                String key = (String) it.next();
                request.queryString(key, requestData.get(key));
            }
            HttpResponse<String> response = request.asString();
            if (response.getStatus() == 200) {
                JSONObject resultJson = new JSONObject(response.getBody());
                result.put("d", resultJson.getJSONArray("results"));
                if (resultJson.has("count")) {
                    result.put("c", resultJson.getInt("count"));
                }
            } else {
                result.put("e", new JSONObject(response.getBody()));
            }
        } catch (UnirestException e) {
            JSONObject errorCode = Utils.exceptionToCode(e);
            result.put("e", errorCode);
            if (errorCode.optInt("code") == 0) {
                LOGGER.log(Level.WARNING, "Server communication failed", e);
            }
            return new JSONObject(result);
        } catch (JSONException e) {
            LOGGER.log(Level.WARNING, "Converting to json failed", e);
        }
        return new JSONObject(result);
    }
}
