package org.parsejava;

import org.json.JSONObject;

import java.util.HashMap;

import static org.parsejava.restexecutors.ObjectRelated.*;


/**
 * @author mrisvanv
 * @since 2020-04-24
 */
public class ParseObject {
    protected final HashMap<String, Object> data = new HashMap<>();
    protected final String className;
    protected int processType = 0;
    String objectId = null;

    public ParseObject(String className) {
        this.className = className;
    }

    public void put(String key, Object value) {
        if (ParseFile.class.equals(value.getClass())) {
            value = ((ParseFile) value).getFileObject();
        } else if (ParsePointer.class.equals(value.getClass())) {
            value = ((ParsePointer) value).get();
        }
        data.put(key, value);
    }

    public void set(String objectId) {
        this.objectId = objectId;
    }

    public JSONObject save() {
        if (objectId == null) {
            return restObjectPost(new JSONObject(data), className);
        } else {
            return restObjectPut(new JSONObject(data), className, objectId);
        }
    }

    public JSONObject delete() {
        return restObjectDelete(className, objectId);
    }

    public void forAdd() {
        processType = 1;
    }

    public void forUpdate() {
        processType = 2;
    }

    public void forDelete() {
        processType = 3;
    }


}