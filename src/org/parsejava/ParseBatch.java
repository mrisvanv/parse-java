package org.parsejava;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.parsejava.Parse.DB_NAME;
import static org.parsejava.restexecutors.BatchRelated.restBatchPost;


/**
 * @author mrisvanv
 * @since 2020-04-28
 */
public class ParseBatch {
    final List<JSONObject> objects = new ArrayList<>();

    public void put(ParseObject object) {
        HashMap<String, Object> objectData = new HashMap<>();
        String method = "";
        switch (object.processType) {
            case 1: {
                method = "POST";
                objectData.put("path", "/" + DB_NAME + "/classes/" + object.className.trim());
                break;
            }
            case 2: {
                method = "PUT";
                objectData.put("path", "/" + DB_NAME + "/classes/" + object.className.trim() + "/" + object.objectId.trim());
                break;
            }
            case 3: {
                method = "DELETE";
                objectData.put("path", "/" + DB_NAME + "/classes/" + object.className.trim() + "/" + object.objectId.trim());
                break;
            }
        }
        objectData.put("method", method);
        objectData.put("body", new JSONObject(object.data));
        objects.add(new JSONObject(objectData));
    }

    public JSONObject processBatch() {
        HashMap<String, Object> requests = new HashMap<>();
        requests.put("requests", new JSONArray(objects));
        return restBatchPost(new JSONObject(requests));
    }
}
