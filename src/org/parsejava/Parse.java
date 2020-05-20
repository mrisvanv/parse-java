package org.parsejava;

import java.util.logging.Logger;

/**
 * @author mrisvanv
 * @since 2020-04-17
 */
public class Parse {
    public static String APP_ID = null;
    public static String CLIENT_ID = null;
    public static String SERVER_URL = null;
    public static String DB_NAME = null;

    public static Logger PARSE_LOGGER = Logger.getLogger("parse-java");

    public static void initialize(String appId, String clientKey, String serverUrl) {
        APP_ID = appId;
        CLIENT_ID = clientKey;
        SERVER_URL = serverUrl;
        String[] urls = serverUrl.split("/");
        DB_NAME = urls[urls.length - 1];
    }

    public static void setLogger(Logger logger) {
        PARSE_LOGGER = logger;
    }
}
