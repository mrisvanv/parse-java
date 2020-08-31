package org.parsejava.restexecutors;


import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequestWithBody;
import org.json.JSONException;
import org.json.JSONObject;
import org.parsejava.Parse;
import org.parsejava.ParseUser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.parsejava.Parse.MASTER_ID;
import static org.parsejava.Parse.PARSE_LOGGER;

/**
 * @author risvan.v
 * @since 2020-05-28
 */
public class FileRelated {
    static final Logger LOGGER = PARSE_LOGGER;
    private static final String CLASS_PATH = Parse.SERVER_URL + "/files/";

    public static JSONObject restFilePost(byte[] data, String fileName) {
        HashMap<String, Object> result = new HashMap<>();
        try {
            HttpRequestWithBody request = Unirest.post(CLASS_PATH + fileName);
            request.header(Constants.HEADER_APP_ID, Parse.APP_ID)
                    .header(Constants.HEADER_CLIENT_KEY, Parse.CLIENT_ID);
            if (Parse.MASTER_ID != null && !Parse.MASTER_ID.equals("")) {
                request.header(Constants.HEADER_MASTER_KEY, MASTER_ID);
            }
            String sessionId = ParseUser.getCurrentUser().getSessionId();
            if (sessionId != null && !sessionId.trim().equals("")) {
                request.header(Constants.HEADER_SESSION_TOKEN, sessionId);
            }
//            String contentType = fileNameTOMime(fileName);
//            request.header(Constants.CONTENT_TYPE, contentType.trim());
            request.header("Content-Type", "image/jpeg");
            request.header("Cache-Control", "no-cache");
            request.body(data);
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
        } catch (Exception e) {
            JSONObject errorCode = Utils.exceptionToCode(e);
            result.put("e", errorCode);
            LOGGER.log(Level.WARNING, "Not a valid file name", e);
        }
        return new JSONObject(result);
    }

    public static JSONObject restFileDelete(String fileName) {
        HashMap<String, Object> result = new HashMap<>();
        try {
            HttpRequestWithBody request = Unirest.delete(CLASS_PATH + fileName);
            request.header(Constants.HEADER_APP_ID, Parse.APP_ID)
                    .header(Constants.HEADER_CLIENT_KEY, Parse.CLIENT_ID);
            if (Parse.MASTER_ID != null && !Parse.MASTER_ID.equals("")) {
                request.header(Constants.HEADER_MASTER_KEY, MASTER_ID);
            }
            String sessionId = ParseUser.getCurrentUser().getSessionId();
            if (sessionId != null && !sessionId.trim().equals("")) {
                request.header(Constants.HEADER_SESSION_TOKEN, sessionId);
            }
//            String contentType = fileNameTOMime(fileName);
//            request.header(Constants.CONTENT_TYPE, contentType.trim());
            request.header("Cache-Control", "no-cache");
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
        } catch (Exception e) {
            JSONObject errorCode = Utils.exceptionToCode(e);
            result.put("e", errorCode);
            LOGGER.log(Level.WARNING, "Not a valid file name", e);
        }
        return new JSONObject(result);
    }

    private static String fileNameTOMime(String fileName) throws IOException {
        Path path = new File(fileName).toPath();
        String mimeType = Files.probeContentType(path);
        return mimeType;
    }
}
