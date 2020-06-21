package org.parsejava;

import org.json.JSONObject;

import java.util.HashMap;

public class ParsePointer {
    protected final HashMap<String, Object> pointer = new HashMap<>();
    public ParsePointer(String objectId, String className){
        pointer.put("__type","Pointer");
        pointer.put("objectId",objectId);
        pointer.put("className",className);
    }
    public JSONObject get(){
        return new JSONObject(pointer);
    }
}
