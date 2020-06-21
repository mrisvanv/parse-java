package org.parsejava;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.parsejava.restexecutors.QueryRelated.restGet;

/**
 * @author mrisvanv
 * @since 2020-04-24
 */
public class ParseQuery {
    private final HashMap<String, Object> whereConditions = new HashMap<>();
    private final List<String> includes = new ArrayList<>();
    private String className = null;
    private long limit = 10000000;//set as default value
    private String order = "";
    private long skip = 0;
    private boolean isCount = false;

    public static ParseQuery getQuery(String className) {
        ParseQuery query = new ParseQuery();
        query.setClassName(className);
        return query;
    }

    private String getClassName() {
        return className;
    }

    private void setClassName(String className) {
        this.className = className;
    }

    public void whereEqualTo(String objectName, Object value) {
        if(value.getClass()==ParsePointer.class){
            value=((ParsePointer)value).get();
        }
        whereConditions.put(objectName, value);
    }

    public void setLimit(long limit) {
        this.limit = limit;
    }

    public void setSkip(long skip) {
        this.skip = skip;
    }

    public void orderByAscending(String order) {
        this.order = order;
    }

    public void orderByDescending(String order) {
        this.order = "-" + order;
    }

    public void addAscendingOrder(String order) {
        this.order += "," + order;
    }

    public void addDescendingOrder(String order) {
        this.order += ",-" + order;
    }

    public void include(String includeColumn){
        includes.add(includeColumn);
    }
    public void whereContainedIn(String objectName, List<Object> values) {
        HashMap<String, Object> inList = new HashMap<>();
        inList.put("$in", new JSONArray(values));
        whereConditions.put(objectName, new JSONObject(inList));
    }

    public void whereMatches(String objectName, String values) {
        HashMap<String, Object> inList = new HashMap<>();
        inList.put("$regex", values);
        whereConditions.put(objectName, new JSONObject(inList));
    }


    public JSONObject find() {
        HashMap<String, Object> request = new HashMap<>();
        request.put("where", new JSONObject(whereConditions));
        request.put("limit", limit);
        if (!order.equals("")) {
            request.put("order", order);
        }
        if (skip != 0) {
            request.put("skip", skip);
        }
        if (isCount) {
            request.put("count", 1);
        }
        if(includes.size()>0){
            String includeColumns=includes.toString();
            request.put("include", includeColumns.substring(1,includeColumns.length()-1).replace(" ",""));
        }
        return restGet(new JSONObject(request), this.className);
    }

    public JSONObject findAndCount() {
        this.isCount = true;
        return find();
    }
}
