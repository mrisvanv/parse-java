package org.parsejava;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

import static org.parsejava.restexecutors.UserRelated.doLogin;
import static org.parsejava.restexecutors.UserRelated.doLogout;


/**
 * @author mrisvanv
 * @since 2020-04-17
 */
public class ParseUser {
    public static String errMsg = null;
    private static ParseUser currentUser = new ParseUser();
    private String username = null;
    private String userId = null;
    private String email = null;
    private String sessionId = null;
    private HashMap<String, Object> objects;

    public static ParseUser getCurrentUser() {
        return currentUser;
    }

    public static boolean logIn(String username, String password) {
        ParseUser user = new ParseUser();
        JSONObject result = doLogin(username, password);
        if (result == null) {
            errMsg = "Unknown error";
            return false;
        } else if (result.has("e")) {
            errMsg = result.optJSONObject("e").optString("error");
            return false;
        } else {
            errMsg = null;
            if (result.optString("status").equals("failed")) {
                errMsg = result.optString("error");
                return false;
            }
            user.setUsername(result.optString("username"));
            user.setSessionId(result.optString("sessionToken"));
            user.setEmail(result.optString("email"));
            user.setUserId(result.optString("objectId"));
            Iterator<String> keys = result.keys();
            HashMap<String, Object> map = new HashMap<>();
            while (keys.hasNext()) {
                String key = keys.next();
                map.put(key, result.opt(key));
            }
            user.setObjects(map);
            currentUser = user;
            return true;
        }
    }

    public static boolean become(String sessionId) {
        ParseUser user = new ParseUser();
        JSONObject result = doLogin(sessionId);
        if (result == null) {
            errMsg = "Unknown error";
            return false;
        } else {
            errMsg = null;
            if (result.optString("status").equals("failed")) {
                errMsg = result.optString("error");
                return false;
            }
            user.setUsername(result.optString("username"));
            user.setSessionId(result.optString("sessionToken"));
            user.setEmail(result.optString("email"));
            user.setUserId(result.optString("objectId"));
            Iterator<String> keys = result.keys();
            HashMap<String, Object> map = new HashMap<>();
            while (keys.hasNext()) {
                String key = keys.next();
                map.put(key, result.opt(key));
            }
            user.setObjects(map);
            currentUser = user;
            return true;
        }
    }

    public static boolean logOut() {
        if (getCurrentUser() != null && getCurrentUser().getSessionId() != null) {
            JSONObject result = doLogout(getCurrentUser().getSessionId());
            if (result == null) {
                errMsg = "Unknown error";
                return false;
            } else {
                errMsg = null;
                if (result.optString("status").equals("failed")) {
                    errMsg = result.optString("error");
                    return false;
                }
                currentUser = new ParseUser();
                return true;
            }
        }
        return true;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public HashMap<String, Object> getObjects() {
        return objects;
    }

    public void setObjects(HashMap<String, Object> objects) {
        this.objects = objects;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
