package org.parsejava;

import org.json.JSONObject;
import org.parsejava.restexecutors.FileRelated;

import java.util.HashMap;

import static org.parsejava.restexecutors.Utils.exceptionToCodeWithE;

/**
 * @author risvan.v
 * @since 2020-05-28
 */
public class ParseFile {
    private String fileName;
    private byte[] fileData;
    private HashMap<String, String> object = null;

    public ParseFile() {
    }

    public ParseFile(String fileName, byte[] fileData) {
        this.fileName = fileName;
        this.fileData = fileData;
    }

    public JSONObject save() {
        JSONObject result = FileRelated.restFilePost(fileData, fileName);
        if (!result.has("e")) {
            JSONObject serverFileData = result.optJSONObject("d");
            if (serverFileData != null) {
                String fileFullName = serverFileData.optString("name");
                String fileUrl = serverFileData.optString("url");
                if (fileFullName.equals("") || fileUrl.equals("")) {
                    return exceptionToCodeWithE("Failed to add image");
                } else {
                    object = new HashMap<>();
                    object.put("__type", "File");
                    object.put("name", fileFullName);
                    object.put("url", fileUrl);
                }
            } else {
                return exceptionToCodeWithE("Failed to add image");
            }
        }
        return result;
    }

    JSONObject getFileObject() {
        return new JSONObject(object);
    }

    public boolean hasFile() {
        return (object != null);
    }
}
