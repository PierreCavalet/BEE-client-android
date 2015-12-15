package fr.pierrecavalet.bestexcuseever.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Pierre on 25/10/2015.
 */
public class Comment {

    private String username;
    private String content;
    private String time;

    public Comment(String username, String content) {
        this.username = username;
        this.content = content;
    }

    public Comment(JSONObject beeJSONObject) throws JSONException {
        this.username = (String) beeJSONObject.get("user");
        this.time = (String) beeJSONObject.get("time");
        this.content = (String) beeJSONObject.get("content");
    }

    public String getUsername() {
        return username;
    }

    public String getContent() {
        return content;
    }
}
